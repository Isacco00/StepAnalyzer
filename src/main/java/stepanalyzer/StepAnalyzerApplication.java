package stepanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StepAnalyzerApplication {
/*
	private static final Logger LOGGER = LoggerFactory.getLogger(StepAnalyzerApplication.class);
	@Inject
	private ForexSentimentManager manager;*/

	public static void main(String[] args) {
		SpringApplication.run(StepAnalyzerApplication.class, args);
	}
/*
	@EventListener(ApplicationReadyEvent.class)
	public void startToSaveDataInDb() {
		// Saving for live update
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// Parameters: 1, Task body 2, Delay time for first execution
		// 3. Task Execution Interval 4. Interval Units
		service.scheduleAtFixedRate(() -> saveLastForexSentimentToDb(false), 0, 10, TimeUnit.MINUTES);

		// Clear Every nigth sentiment table
//		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime nextDelete = now.withHour(0).withMinute(0).withSecond(0);
//		if (now.compareTo(nextDelete) > 0)
//			nextDelete = nextDelete.plusDays(1);
//		Duration duration = Duration.between(now, nextDelete);
//		long initalDelay = duration.getSeconds();
//		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//		scheduler.scheduleAtFixedRate(() -> deleteDailySentimentFromDb(), initalDelay, TimeUnit.DAYS.toSeconds(1),
//				TimeUnit.SECONDS);

		// Saving history sentiments every day at 15.30
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextRun = now.withHour(15).withMinute(30).withSecond(0);
		if (now.compareTo(nextRun) > 0)
			nextRun = nextRun.plusDays(1);
		Duration d = Duration.between(now, nextRun);
		long id = d.getSeconds();
		ScheduledExecutorService s = Executors.newScheduledThreadPool(1);
		s.scheduleAtFixedRate(() -> saveLastForexSentimentToDb(true), id, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
	}

	private void saveLastForexSentimentToDb(boolean isHistorySaving) {
		LOGGER.info("Starting save new entity at " + LocalDateTime.now());
		manager.saveLastForexSentimentToDb(isHistorySaving);
		LOGGER.info("Save completed at " + LocalDateTime.now());
	}*/
}
