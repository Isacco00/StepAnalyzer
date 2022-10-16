package forexsentiment.manager.impl;

import forexsentiment.bean.OptimizationBean;
import forexsentiment.manager.OptimizationManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OptimizationManagerImpl implements OptimizationManager {

    @Override
    public List<OptimizationBean> downloadOptimization(String fileUrl) throws MalformedURLException {
        List<OptimizationBean> optimizationBeans = new ArrayList<>();
        URL url = new URL(fileUrl);
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
        long counter = 0L;
        try (CSVParser csvParser = CSVParser.parse(url, StandardCharsets.UTF_8, csvFormat)) {
            for (CSVRecord csvRecord : csvParser) {
                OptimizationBean bean = new OptimizationBean();
                bean.setTokenOptimization(counter++);
                bean.setActivePair(Boolean.parseBoolean(csvRecord.get("ACTIVE PAIR")));
                bean.setFirstRangeSentiment(Integer.parseInt(csvRecord.get("IN")));
                bean.setSecondRangeSentiment(Integer.parseInt(csvRecord.get("OUT")));
                bean.setUseBreakeven(Boolean.parseBoolean(csvRecord.get("BE")));
                optimizationBeans.add(bean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return optimizationBeans;
    }
}
