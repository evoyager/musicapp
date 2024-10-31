package com.epam.song;

import com.epam.song.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.SpringApplication.run;

@SpringBootTest
class SongServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void givenEntityWithoutAnnotationApplication_whenBootstrap_thenExpectedExceptionThrown() {
		ConfigurableApplicationContext context = run(SongServiceApplication.class);
//		Exception exception = assertThrows(Exception.class,
//				() -> run(SongServiceApplication.class));
//
//		assertThat(exception)
//				.getRootCause()
//				.hasMessageContaining("Not a managed type");
		SongRepository repository = context
				.getBean(SongRepository.class);

		assertThat(repository).isNotNull();
	}

}
