package in.happy.rest;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.happy.binding.CustomerEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@RestController
public class CustomerRestController {
	@GetMapping(value="/event" , produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<CustomerEvent> getEvent(){
		CustomerEvent event  = new CustomerEvent("jhon",new Date());
		return Mono.justOrEmpty(event);

}
	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CustomerEvent> getEvents() {
	    // Create a CustomerEvent object
	    CustomerEvent event = new CustomerEvent("Smith", new Date());

	    // Create a stream of CustomerEvent objects
	    Stream<CustomerEvent> stream = Stream.generate(() -> event);

	    // Creating Data flux from the stream
	    Flux<CustomerEvent> dataFlux = Flux.fromStream(stream);

	    // Setting Response Interval
	    Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(3));

	    // Zipping the intervalFlux and dataFlux
	    Flux<Tuple2<Long, CustomerEvent>> zip = Flux.zip(intervalFlux, dataFlux);

	    // Mapping the Tuple2 to get the CustomerEvent
	    Flux<CustomerEvent> fluxMap = zip.map(Tuple2::getT2);

	    return fluxMap;
	}

}