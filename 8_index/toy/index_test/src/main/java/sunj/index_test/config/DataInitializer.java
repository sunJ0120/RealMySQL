package sunj.index_test.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import sunj.index_test.domain.Level;
import sunj.index_test.entity.Logs;
import sunj.index_test.repository.LogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 로그 더미 데이터 저장을 위한 클래스
 *
 * ApplicationRunner를 이용해서 스프링 부트가 완전히 시작된 직후 자동으로 시작.
 * 배치 방식으로 묶어서 한 번에 저장
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
    private final LogRepository logRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 애플리케이션을 시작하면서 자동으로 실행된다.
        log.info("더미데이터 생성 시작");

        // 이미 해당하는 데이터가 있을 경우, 실행 된 것이므로 스킵한다.
        if(logRepository.count() > 0){
            log.info("데이터가 이미 존재하므로, 스킵합니다.");
            return;
        }

        // 10만개의 더미 데이터 로그를 생성
        List<Logs> logs = new ArrayList<>();
        Random random = new Random();

        // Level enum의 모든 값을 배열로 가져옴
        Level[] levels = Level.values();  // [ERROR, WARN, INFO, DEBUG]

        for(int i = 1; i <= 100000; i++){
            Logs logEntity = Logs.builder()
                    .dateTime(LocalDateTime.now().minusDays(random.nextInt(30))) //최근 30일을 랜덤하게 넣는다.
                    .level(levels[random.nextInt(levels.length)]) //길이 내에서 랜덤 정수를 생성
                    .userId((long) random.nextInt(10000)) //10000 중에서 랜덤한 숫자를 생성
                    .message("로그 메세지 : " + i)
                    .build();

            logs.add(logEntity); //배열에 더하기

            //배치 방식으로, 1000 단위로 저장
            if(i % 1000 == 0){
                logRepository.saveAll(logs);
                logs.clear();
                log.info("{}개 째 저장 완료", i);
            }
        }
        log.info("더미 데이터 생성 완료.");
    }
}
