package com.khan.vaquar.dataloader.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.khan.vaquar.dataloader.domain.Person;

/**
 * 
 * @author vaquar khan
 *
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(final Person person) throws Exception {
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();
		// try {
		if (firstName.isEmpty()) {
			throw new Exception("Tets exception");
		}

		/*
		 * } catch (Exception e) { String stackTrace = Debugger.stackTrace(e); //
		 * this.logger.error(stackTrace + " ITEMS:" + items); log.error(stackTrace +
		 * " ITEMS:" + firstName); // throw new LoaderException("null pointer",
		 * person.getFirstName(), person, e);
		 * 
		 * }
		 */

		final Person transformedPerson = new Person(firstName, lastName);

		// log.info("Converting (" + person + ") into (" + transformedPerson + ")");

		return transformedPerson;
	}

}
