package stepanalyzer.utility;

import org.springframework.stereotype.Component;
import stepanalyzer.exception.ValidationException;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

@Component
public class StepUtility {

    @Inject
    CalcUtility calcUtility;

    public String processStepFile(InputStream file) throws IOException {
        final FileHandler fileHandler = new FileHandler();
        Map<String, String> stepBean = loadFileAndMapIntoBean(fileHandler, file);
        return parseStepFile(stepBean);
    }

    private String parseStepFile(Map<String, String> stepBean) throws IOException {
        String closedShellKey = "";
        for (Map.Entry<String, String> row : stepBean.entrySet()) {
            if (row.getValue().contains("CLOSED_SHELL")) {
                closedShellKey = row.getKey();
                break;
            }
        }
        List<String> advancedFaceIdList = new ArrayList<>();
        Matcher matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(stepBean.get(closedShellKey));
        while (matcher.find()) {
            advancedFaceIdList.add(matcher.group(1));
        }
        List<String> pointSet = new ArrayList<>();
        List<String> indexLineSet = new ArrayList<>();
        List<String> indexFaceSet = new ArrayList<>();
        int cordIndex = 0;
        int indexedFaceSetStart = cordIndex;
        for (int j = 0; j < advancedFaceIdList.size(); j++) {
            String advancedFace = stepBean.get(advancedFaceIdList.get(j));
            List<String> boundsId = new ArrayList<>();
            matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(advancedFace);
            while (matcher.find()) {
                boundsId.add(matcher.group(1));
            }
            String faceGeometryId = boundsId.get(boundsId.size() - 1);
            String faceGeometry = stepBean.get(faceGeometryId);
            boundsId.remove(boundsId.size() - 1);
            List<String> orientedEdgeIdList = new ArrayList<>();
            for (String boundId : boundsId) {
                String faceOuterBound = stepBean.get(boundId);
                List<String> edgeLoopIdList = new ArrayList<>();
                matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(faceOuterBound);
                while (matcher.find()) {
                    edgeLoopIdList.add(matcher.group(1));
                }
                for (String edgeLoopId : edgeLoopIdList) {
                    String edgeLoop = stepBean.get(edgeLoopId);
                    matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(edgeLoop);
                    while (matcher.find()) {
                        orientedEdgeIdList.add(matcher.group(1));
                    }
                }
            }
            List<String> edgeCurveIdList = new ArrayList<>();
            for (String id : orientedEdgeIdList) {
                String orientedEdge = stepBean.get(id);
                matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(orientedEdge);
                while (matcher.find()) {
                    edgeCurveIdList.add(matcher.group(1));
                }
            }
            List<String> edgeGeometryIdList = new ArrayList<>();
            for (String id : edgeCurveIdList) {
                String edgeCurve = stepBean.get(id);
                matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(edgeCurve);
                while (matcher.find()) {
                    edgeGeometryIdList.add(matcher.group(1));
                }
            }
            double perimeter = 0.0;
            StringBuilder perimeterValue = new StringBuilder();
            for (int i = 0; i < edgeGeometryIdList.size(); i++) {
                String id = edgeGeometryIdList.get(i);
                String edgeGeometry = stepBean.get(id);
                if (edgeGeometry.contains("LINE")) {
                    indexLineSet.add(Integer.toString(cordIndex));
                    cordIndex++;
                    indexLineSet.add(Integer.toString(cordIndex));
                    cordIndex++;
                    indexLineSet.add("-1");
                    List<String[]> cartesianPoint = getCartesianPoint(stepBean, edgeGeometryIdList, i);
                    pointSet.add(Float.parseFloat(cartesianPoint.get(0)[1]) + " " + Float.parseFloat(cartesianPoint.get(0)[2]) + " " + CalcUtility.round(Float.parseFloat(cartesianPoint.get(0)[3]), 2));
                    pointSet.add(Float.parseFloat(cartesianPoint.get(1)[1]) + " " + Float.parseFloat(cartesianPoint.get(1)[2]) + " " + CalcUtility.round(Float.parseFloat(cartesianPoint.get(1)[3]), 2));
                    double value = calcDistanceBetweenPoints(cartesianPoint.get(0), cartesianPoint.get(1));

//                    perimeter = perimeter + value;
                }
                if (edgeGeometry.contains("CIRCLE")) {
                    String centerId = edgeGeometry.replaceAll("([A-Z])+\\(|([#;)])", "").split(",")[1];
                    String center = stepBean.get(centerId);
                    matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(center);
                    List<String> centerIdDetail = new ArrayList<>();
                    while (matcher.find()) {
                        centerIdDetail.add(matcher.group(1));
                    }
                    String[] cartesianPointCenterValues = stepBean.get(centerIdDetail.get(0)).replaceAll("([()'#;])", "").split(",");
                    double direction = Float.parseFloat(stepBean.get(centerIdDetail.get(1)).replaceAll("([()'#;])", "").split(",")[3]);
                    double centerX = Float.parseFloat(cartesianPointCenterValues[1]);
                    double centerY = Float.parseFloat(cartesianPointCenterValues[2]);
                    double centerZ = Float.parseFloat(cartesianPointCenterValues[3]);
                    double radius = Double.parseDouble(edgeGeometry.replaceAll("([A-Z])+\\(|([#;)])", "").split(",")[2]);
                    List<String[]> cartesianPoint = getCartesianPoint(stepBean, edgeGeometryIdList, i);
                    double startX = Float.parseFloat(cartesianPoint.get(0)[1]);
                    double startY = Float.parseFloat(cartesianPoint.get(0)[2]);
                    double startZ = Float.parseFloat(cartesianPoint.get(0)[3]);
                    double endX = Float.parseFloat(cartesianPoint.get(1)[1]);
                    double endY = Float.parseFloat(cartesianPoint.get(1)[2]);
                    double endZ = Float.parseFloat(cartesianPoint.get(1)[3]);
                    double deltaStartX = CalcUtility.round(((startX - centerX) / radius), 2); //Cos
                    double deltaStartY = CalcUtility.round(((startY - centerY) / radius), 2); //Sin
                    double deltaEndX = CalcUtility.round(((endX - centerX) / radius), 2); //Cos
                    double deltaEndY = CalcUtility.round(((endY - centerY) / radius), 2); //Sin
                    int angleStart = angleFromSinCos(deltaStartY, deltaStartX);
                    int angleEnd = angleFromSinCos(deltaEndY, deltaEndX);
                    //if ((startX == endX && startY == endY) || (angleStart == 0 && angleEnd == 0) || (direction == 1 && angleEnd == 0)) {
                    //angleEnd = 360;
                    //}
                    int angleIncrement = 1, counterOperations = 0;
                    List<Integer> angleList = new ArrayList<>();
                    angleList.add(angleStart);
                    do {
                        if (direction == -1) { //Orario
                            angleStart -= angleIncrement;
                        } else if (direction == 1) { //Antiorario
                            angleStart += angleIncrement;
                        } else {
                            throw new ValidationException("Errore direzione cerchio");
                        }
                        if (angleStart < 0) {
                            angleStart = 360 + angleStart;
                        }
                        if (angleStart >= 360) {
                            angleStart = angleStart - 360;
                        }
                        if (angleStart % 9 == 0) {
                            angleList.add(angleStart);
                        }
                        counterOperations++;
                        if (counterOperations > 360) {
                            break;
                        }
                    } while (angleStart != angleEnd);
                    for (Integer angle : angleList) {
                        double incrementX = (Math.sin(Math.toRadians(angle)) * radius);
                        double incrementY = (Math.cos(Math.toRadians(angle)) * radius);
                        double incrementZ = 0;
                        double newPointX = calcUtility.roundNDecimal(centerX + incrementY, 2);
                        double newPointY = calcUtility.roundNDecimal(centerY + incrementX, 2);
                        double newPointZ = calcUtility.roundNDecimal(centerZ + incrementZ, 2);
                        pointSet.add(newPointX + " " + newPointY + " " + newPointZ);
                        indexLineSet.add(Integer.toString(cordIndex));
                        cordIndex++;
                    }
                    indexLineSet.add("-1");
//                    double distance = calcDistanceBetweenPoints(cartesianPoint.get(0), cartesianPoint.get(1));
                    double value;
//                    if (distance == 0) {
//                        value = radius * 2 * Math.PI;
//                    } else {
//                        value = radius * 2 * Math.asin(distance / (2 * radius));
//                    }
//                    perimeter = perimeter + value;
//                    perimeterValue.append("+").append(" from circle ").append(value);
                }
            }
//            faceGeometryFound.add("");
//            faceGeometryFound.add("---------------------------------------------------------------------");
//            faceGeometryFound.add("PERIMETRO FACCIA : " + perimeter);
//            faceGeometryFound.add("PERIMETRO VALORI : " + perimeterValue);
//            faceGeometryFound.add("---------------------------------------------------------------------");
//            faceGeometryFound.add("");

//            counter++;
            //Z Reset

            //IndexedFaceSet
            int lastIndex = cordIndex - 1;
            int firstIndex = indexedFaceSetStart;
            for (; indexedFaceSetStart < lastIndex; indexedFaceSetStart++) {
                if (indexedFaceSetStart == lastIndex - 1) {
                    indexFaceSet.add(indexedFaceSetStart + "," + firstIndex + "," + lastIndex + ",-1\n");
                } else {
                    indexFaceSet.add(indexedFaceSetStart + "," + (indexedFaceSetStart + 1) + "," + lastIndex + ",-1\n");
                }
            }
            indexedFaceSetStart = cordIndex;

        }
        String html = convertToHtml(String.join(", ", indexLineSet), String.join(",\n ", pointSet), String.join(", ", indexFaceSet));
        String x3d = convertToX3D(String.join(", ", indexLineSet), String.join(",\n ", pointSet), String.join(", ", indexFaceSet));
        PrintWriter writer = new PrintWriter("C:\\Users\\isacco\\Downloads\\test.xhtml", StandardCharsets.UTF_8);
        writer.println(html);
        writer.close();
        String inputFile = "C:\\Users\\isacc\\Desktop\\3DModelsToConvert\\Converted-STLs\\100x80x60r60esterno.stp.stl";
        return x3d;
    }

    private String convertToHtml(String indexLineSet, String pointSet, String indexFaceSet) {
        String position = "\"147.98 -57.9795 127.98\"";
        String string = """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                <html xmlns='http://www.w3.org/1999/xhtml'>
                <head>
                \t<script type='text/javascript' src='""" + System.getProperty("user.home") + "\\Desktop\\x3dom.js" + """
                '/>
                \t<link rel='stylesheet' type='text/css' href='""" + System.getProperty("user.home") + "\\Desktop\\x3dom.css" + """
                '/>
                </head>
                <X3D profile="Immersive" version="3.2" xmlns:xsd="http://www.w3.org/2001/XMLSchema-instance" xsd:noNamespaceSchemaLocation="https://www.web3d.org/specifications/x3d-3.2.xsd" width="1280px"  height="1024px">
                \t<head>
                \t</head>
                \t<Scene>
                \t\t<Viewpoint id="Iso" centerOfRotation="0 0 0" position=""" + position + """
                \sorientation="0.742906 0.307722 0.594473 1.21712" description="camera" fieldOfView="0.9"></Viewpoint>
                \t\t<Transform DEF="o1" translation="0 0 0" rotation="0 0 1  0" scale="1 1 1" scaleOrientation="0 0 1  0" center="0 0 0" >
                \t\t\t<Group DEF="o2">
                \t\t\t\t<Shape DEF="o3">
                \t\t\t\t<Appearance DEF="o4">
                \t\t\t\t<Material DEF="o5" emissiveColor="0.098039217 0.098039217 0.098039217" />
                \t\t\t\t</Appearance>
                \t\t\t\t<PointSet DEF="o6">
                \t\t\t\t<Coordinate DEF="o7" point="\s""" + pointSet + """
                        " />
                \t\t\t\t</PointSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>
                \t\t\t<Group DEF="o8">
                \t\t\t\t<Shape DEF="o9">
                \t\t\t\t<Appearance DEF="o10">
                \t\t\t\t<Material DEF="o11" diffuseColor="0.098039217 0.098039217 0.098039217" shininess="1" />
                \t\t\t\t</Appearance>
                \t\t\t\t<IndexedLineSet DEF="o12" coordIndex="\s""" + indexLineSet + """
                ">
                \t\t\t\t<Coordinate USE="o7" />
                \t\t\t\t</IndexedLineSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>
                \t\t\t<!--<Group DEF="o13">
                \t\t\t\t<Shape DEF="o14">
                \t\t\t\t<Appearance DEF="o15">
                \t\t\t\t<Material DEF="o16"/>
                \t\t\t\t</Appearance>
                \t\t\t\t<IndexedFaceSet DEF="o17" coordIndex="\s""" + indexFaceSet + """ 
                " ccw="TRUE" solid="FALSE" convex="TRUE" creaseAngle="0.5" >
                \t\t\t\t<Coordinate USE="o7" />
                \t\t\t\t</IndexedFaceSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>-->
                \t\t</Transform>
                \t</Scene>
                </X3D>
                </html>
                """;
        return string;
    }
    private String convertToX3D(String indexLineSet, String pointSet, String indexFaceSet) {
        String position = "\"147.98 -57.9795 127.98\"";
        String string = """
                <X3D profile="Immersive" version="3.2" xmlns:xsd="http://www.w3.org/2001/XMLSchema-instance" xsd:noNamespaceSchemaLocation="https://www.web3d.org/specifications/x3d-3.2.xsd" width="1280px"  height="1024px">
                \t<head>
                \t</head>
                \t<Scene>
                \t\t<Viewpoint id="Iso" centerOfRotation="0 0 0" position=""" + position + """
                \sorientation="0.742906 0.307722 0.594473 1.21712" description="camera" fieldOfView="0.9"></Viewpoint>
                \t\t<Transform DEF="o1" translation="0 0 0" rotation="0 0 1  0" scale="1 1 1" scaleOrientation="0 0 1  0" center="0 0 0" >
                \t\t\t<Group DEF="o2">
                \t\t\t\t<Shape DEF="o3">
                \t\t\t\t<Appearance DEF="o4">
                \t\t\t\t<Material DEF="o5" emissiveColor="0.098039217 0.098039217 0.098039217" />
                \t\t\t\t</Appearance>
                \t\t\t\t<PointSet DEF="o6">
                \t\t\t\t<Coordinate DEF="o7" point="\s""" + pointSet + """
                        " />
                \t\t\t\t</PointSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>
                \t\t\t<Group DEF="o8">
                \t\t\t\t<Shape DEF="o9">
                \t\t\t\t<Appearance DEF="o10">
                \t\t\t\t<Material DEF="o11" diffuseColor="0.098039217 0.098039217 0.098039217" shininess="1" />
                \t\t\t\t</Appearance>
                \t\t\t\t<IndexedLineSet DEF="o12" coordIndex="\s""" + indexLineSet + """
                ">
                \t\t\t\t<Coordinate USE="o7" />
                \t\t\t\t</IndexedLineSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>
                \t\t\t<!--<Group DEF="o13">
                \t\t\t\t<Shape DEF="o14">
                \t\t\t\t<Appearance DEF="o15">
                \t\t\t\t<Material DEF="o16"/>
                \t\t\t\t</Appearance>
                \t\t\t\t<IndexedFaceSet DEF="o17" coordIndex="\s""" + indexFaceSet + """ 
                " ccw="TRUE" solid="FALSE" convex="TRUE" creaseAngle="0.5" >
                \t\t\t\t<Coordinate USE="o7" />
                \t\t\t\t</IndexedFaceSet>
                \t\t\t\t</Shape>
                \t\t\t</Group>-->
                \t\t</Transform>
                \t</Scene>
                </X3D>
                """;
        return string;
    }
    int angleFromSinCos(double sinX, double cosX) {
        double angFromCos = acos(cosX);
        double angFromSin = asin(sinX);
        double sin2 = sinX * sinX;
        if (sinX < 0) {
            angFromCos = -angFromCos;
            if (cosX < 0) //both negative
                angFromSin = -PI - angFromSin;
        } else if (cosX < 0)
            angFromSin = PI - angFromSin;
        //now favor the computation coming from the
        //smaller of sinX and cosX, as the smaller
        //the input value, the smaller the error
        return (int) (((1.0 - sin2) * angFromSin + sin2 * angFromCos) * 180 / PI);
    }

    private static Map<String, String> loadFileAndMapIntoBean(final FileHandler fileHandler, final InputStream file) throws IOException {
        Map<String, String> mapBean = new HashMap<>();
        final BufferedReader br = fileHandler.getFileReader(file);
        String line;
        boolean findContent = false, endContent = false;
        String lastID = "0";
        while ((line = br.readLine()) != null && !endContent) {
            if (findContent) {
                if (line.equals("ENDSEC;")) {
                    //Finish content
                    endContent = true;
                }
                if (line.charAt(0) == '#') {
                    if (line.contains("=")) {
                        String id = line.substring(1, line.indexOf("="));
                        mapBean.put(id, line.substring(line.indexOf("=") + 1));
                        lastID = id;
                    } else {
                        String valueToConcat = mapBean.get(lastID);
                        valueToConcat = valueToConcat.concat(line);
                        mapBean.replace(lastID, valueToConcat);
                    }
                } else {
                    String valueToConcat = mapBean.get(lastID);
                    valueToConcat = valueToConcat.concat(line);
                    mapBean.replace(lastID, valueToConcat);
                }
            }
            if (line.equals("DATA;")) {
                //Start mapping into bean
                findContent = true;
            }
        }
        br.close();
        return mapBean;
    }

    private List<String[]> getCartesianPoint(Map<String, String> stepBean, List<String> edgeGeometryIdList, int i) {
        String cartesianPoint1Id = "", cartesianPoint2Id = "";
        Matcher matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(stepBean.get(edgeGeometryIdList.get(i - 2)));
        if (matcher.find()) {
            cartesianPoint1Id = matcher.group(1);
        }
        matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(stepBean.get(edgeGeometryIdList.get(i - 1)));
        if (matcher.find()) {
            cartesianPoint2Id = matcher.group(1);
        }
        String cartesianPoint1 = stepBean.get(cartesianPoint1Id);
        String cartesianPoint2 = stepBean.get(cartesianPoint2Id);
        String[] cartesianPoint1Values = cartesianPoint1.replaceAll("([()'#;])", "").split(",");
        String[] cartesianPoint2Values = cartesianPoint2.replaceAll("([()'#;])", "").split(",");
        return Arrays.asList(cartesianPoint1Values, cartesianPoint2Values);
    }

    private static double calcDistanceBetweenPoints(String[] cartesianPoint1Values, String[] cartesianPoint2Values) {
        double v = Math.pow((Double.parseDouble(cartesianPoint2Values[1]) - Double.parseDouble(cartesianPoint1Values[1])), 2) + Math.pow(
                (Double.parseDouble(cartesianPoint2Values[2]) - Double.parseDouble(cartesianPoint1Values[2])), 2) + Math.pow(
                (Double.parseDouble(cartesianPoint2Values[3]) - Double.parseDouble(cartesianPoint1Values[3])), 2);
        return Math.sqrt(v);
    }
}
