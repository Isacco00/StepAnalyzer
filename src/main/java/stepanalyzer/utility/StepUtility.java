package stepanalyzer.utility;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StepUtility {

    @Inject
    CalcUtility calcUtility;

    public void processStepFile(InputStream file) throws IOException {
        final FileHandler fileHandler = new FileHandler();
        Map<String, String> stepBean = loadFileAndMapIntoBean(fileHandler, file);
        parseStepFile(stepBean);
    }

    private void parseStepFile(Map<String, String> stepBean) {
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
        int coordIndex = 0;
        for (int j = 0; j < 1 /*advancedFaceIdList.size()*/; j++) {
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
                    indexLineSet.add(Integer.toString(coordIndex));
                    coordIndex++;
                    indexLineSet.add(Integer.toString(coordIndex));
                    coordIndex++;
                    indexLineSet.add("-1");
                    List<String[]> cartesianPoint = getCartesianPoint(stepBean, edgeGeometryIdList, i);
                    pointSet.add(Float.parseFloat(cartesianPoint.get(0)[1]) + " " + Float.parseFloat(cartesianPoint.get(0)[2]) + " " + Float.parseFloat(cartesianPoint.get(0)[3]));
                    pointSet.add(Float.parseFloat(cartesianPoint.get(1)[1]) + " " + Float.parseFloat(cartesianPoint.get(1)[2]) + " " + Float.parseFloat(cartesianPoint.get(1)[3]));
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
                    double centerX = Float.parseFloat(cartesianPointCenterValues[1]);
                    double centerY = Float.parseFloat(cartesianPointCenterValues[2]);
                    double centerZ = Float.parseFloat(cartesianPointCenterValues[3]);
                    double radius = Double.parseDouble(edgeGeometry.replaceAll("([A-Z])+\\(|([#;)])", "").split(",")[2]);
                    List<String[]> cartesianPoint = getCartesianPoint(stepBean, edgeGeometryIdList, i);
                    double startX = Float.parseFloat(cartesianPoint.get(0)[1]);
                    double startY = Float.parseFloat(cartesianPoint.get(0)[2]);
                    double startZ = Float.parseFloat(cartesianPoint.get(0)[3]);
                    int angle = 9, angleStart = 9;
                    do {
                        int multiplier = 1;
                        if(startX < 0){
                            multiplier = -1;
                        }
                        double incrementX = (Math.sin(Math.toRadians(angleStart)) * radius)*multiplier;
                        double incrementY = (Math.cos(Math.toRadians(angleStart)) * radius)*multiplier;
                        double incrementZ = 0;
                        double endX = calcUtility.roundNDecimal(centerX + incrementX, 2);
                        double endY = calcUtility.roundNDecimal(centerY + incrementY, 2);
                        double endZ = calcUtility.roundNDecimal(centerZ + incrementZ, 2);
                        pointSet.add(startX + " " + startY + " " + startZ);
                        pointSet.add(endX + " " + endY + " " + endZ);
                        indexLineSet.add(Integer.toString(coordIndex));
                        coordIndex++;
                        indexLineSet.add(Integer.toString(coordIndex));
                        coordIndex++;
                        startX = endX;
                        startY = endY;
                        startZ = endZ;
                        angleStart += angle;
                    } while (Float.parseFloat(cartesianPoint.get(0)[1]) < 0 ? startY < Float.parseFloat(cartesianPoint.get(1)[1]) : startX < Float.parseFloat(cartesianPoint.get(1)[1]));
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
        }
        System.out.println(indexLineSet.stream().collect(Collectors.joining(", ")));
        System.out.println(pointSet.stream().collect(Collectors.joining(", ")));
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

    /*
        private Node[] buildStep(Map<String, String> stepBean) {
            List<Node> nodes = new ArrayList<>();
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
            int max = 1, previousMax = 1;
            List<String> finalAdvancedFaceId = new ArrayList<>();
            for (String id : advancedFaceIdList) {
                String advancedFace = stepBean.get(id);
                //            buildAdvancedFace(advancedFace);
                List<String> boundsId = new ArrayList<>();
                matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(advancedFace);
                while (matcher.find()) {
                    boundsId.add(matcher.group(1));
                }
                boundsId.remove(boundsId.size() - 1);
                int finalSize = 0;
                for (String boundId : boundsId) {
                    matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(stepBean.get(boundId));
                    List<String> edgeLoopIdList = new ArrayList<>();
                    while (matcher.find()) {
                        edgeLoopIdList.add(matcher.group(1));
                    }
                    for (String edgeLoopId : edgeLoopIdList) {
                        String edgeLoop = stepBean.get(edgeLoopId);
                        List<String> orientedEdgeId = new ArrayList<>();
                        matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(edgeLoop);
                        while (matcher.find()) {
                            orientedEdgeId.add(matcher.group(1));
                        }
                        finalSize = finalSize + orientedEdgeId.size();

                    }
                }
                if (finalSize >= max) {
                    max = finalSize;
                    if (previousMax != max) {
                        finalAdvancedFaceId.clear();
                        previousMax = max;
                    }
                    finalAdvancedFaceId.add(id);
                }
            }
            //exportToCsv(fileHandler, directory, advancedFaceFound, "AdvancedFace");
            //CheckConsistency
            if (finalAdvancedFaceId.size() > 2) {
                throw new RuntimeException();
            }
            List<String> faceGeometryFound = new ArrayList<>();
            int counter = 0;
            List<String> pointPlaneA = new ArrayList<>();
            List<String> pointPlaneB = new ArrayList<>();
            for (String advancedFaceId : finalAdvancedFaceId) {
                String advancedFace = stepBean.get(advancedFaceId);
                List<String> boundsId = new ArrayList<>();
                matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(advancedFace);
                while (matcher.find()) {
                    boundsId.add(matcher.group(1));
                }
                String faceGeometryId = boundsId.get(boundsId.size() - 1);
                String faceGeometry = stepBean.get(faceGeometryId);
                faceGeometryFound.add("#" + advancedFaceId + "= " + advancedFace);
                faceGeometryFound.add("#" + faceGeometryId + "= " + faceGeometry);
                faceGeometryFound.add("");
                boundsId.remove(boundsId.size() - 1);
                List<String> orientedEdgeIdList = new ArrayList<>();
                for (String boundId : boundsId) {
                    String faceOuterBound = stepBean.get(boundId);
                    faceGeometryFound.add("#" + boundId + "= " + faceOuterBound);

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
                        faceGeometryFound.add("#" + edgeLoopId + "= " + edgeLoop);
                    }
                }
                faceGeometryFound.add("");
                List<String> edgeCurveIdList = new ArrayList<>();
                for (String id : orientedEdgeIdList) {
                    String orientedEdge = stepBean.get(id);
                    matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(orientedEdge);
                    while (matcher.find()) {
                        edgeCurveIdList.add(matcher.group(1));
                    }
                    faceGeometryFound.add("#" + id + "= " + orientedEdge);
                }
                faceGeometryFound.add("");
                List<String> edgeGeometryIdList = new ArrayList<>();
                for (String id : edgeCurveIdList) {
                    String edgeCurve = stepBean.get(id);
                    matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(edgeCurve);
                    while (matcher.find()) {
                        edgeGeometryIdList.add(matcher.group(1));
                    }
                    faceGeometryFound.add("#" + id + "= " + edgeCurve);
                }
                faceGeometryFound.add("");
                double perimeter = 0.0;
                StringBuilder perimeterValue = new StringBuilder();
                for (int i = 0; i < edgeGeometryIdList.size(); i++) {
                    String id = edgeGeometryIdList.get(i);
                    String edgeGeometry = stepBean.get(id);
                    faceGeometryFound.add("#" + id + "= " + edgeGeometry);
                    if (edgeGeometry.contains("LINE")) {
                        double value = getCartesianPoint(stepBean, faceGeometryFound, edgeGeometryIdList, i, counter, pointPlaneA, pointPlaneB, "LINE");
                        perimeter = perimeter + value;
                        perimeterValue.append("+").append(" from line ").append(value);
                    }
                    if (edgeGeometry.contains("CIRCLE")) {
                        double distance = getCartesianPoint(stepBean, faceGeometryFound, edgeGeometryIdList, i, counter, pointPlaneA, pointPlaneB, "CIRCLE");
                        double radius = Double.parseDouble(edgeGeometry.replaceAll("([A-Z])+\\(|([#;)])", "").split(",")[2]);
                        String centerId = edgeGeometry.replaceAll("([A-Z])+\\(|([#;)])", "").split(",")[1];
                        String center = stepBean.get(centerId);
                        matcher = Pattern.compile("((?<=#)[^,)#]+)").matcher(center);
                        List<String> centerIdDetail = new ArrayList<>();
                        while (matcher.find()) {
                            centerIdDetail.add(matcher.group(1));
                        }
                        String[] cartesianPointCenterValues = stepBean.get(centerIdDetail.get(0)).replaceAll("([()'#;])", "").split(",");
                        double value;
                        if (distance == 0) {
                            value = radius * 2 * Math.PI;
                        } else {
                            value = radius * 2 * Math.asin(distance / (2 * radius));
                        }
                        SegmentedSphereMesh spheroidMesh = new SegmentedSphereMesh(64, 0, 0, radius, new Point3D(Double.parseDouble(cartesianPointCenterValues[1]), Double.parseDouble(cartesianPointCenterValues[2]), Double.parseDouble(cartesianPointCenterValues[3])));
                        spheroidMesh.setTextureModeNone(Color.color(Math.random(), Math.random(), Math.random()));
                        nodes.add(spheroidMesh);
                        perimeter = perimeter + value;
                        perimeterValue.append("+").append(" from circle ").append(value);
                    }
                }
                faceGeometryFound.add("");
                faceGeometryFound.add("---------------------------------------------------------------------");
                faceGeometryFound.add("PERIMETRO FACCIA : " + perimeter);
                faceGeometryFound.add("PERIMETRO VALORI : " + perimeterValue);
                faceGeometryFound.add("---------------------------------------------------------------------");
                faceGeometryFound.add("");


                counter++;
            }
            TriangulatedMesh customShape = new TriangulatedMesh(pointsFace1, 1);
            System.out.println(pointsFace1);
            System.out.println(pointsFace2);
            //TriangulatedMesh customShape2 = new TriangulatedMesh(pointsFace2, 1);
            customShape.setLevel(0);
            customShape.setCullFace(CullFace.NONE);
            nodes.add(customShape);
            Node[] arrayNodes = nodes.toArray(Node[]::new);
            return arrayNodes;
            //customShape.getTransforms().addAll(new Rotate(-90, Rotate.X_AXIS));

            //PolyLine3D polyLine3D = new PolyLine3D(points, with, Color.STEELBLUE);

            //stepGroup.getChildren().add(customShape);
            //world.getChildren().addAll(stepGroup);


            //        String fileName = directory.getSelectedFile().getName().substring(0, directory.getSelectedFile().getName().length() - 4);
            //        exportToFile(fileHandler, directory, faceGeometryFound, fileName);
            //        exportToFile(fileHandler, directory, pointPlaneA, "PuntiPianoA_" + fileName);
            //        exportToFile(fileHandler, directory, pointPlaneB, "PuntiPianoB_ " + fileName);
        }
    */
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
