package com.concurrency.parllelStream;

import com.concurrency.util.DoubleGenerator;
import com.concurrency.util.Person;
import com.concurrency.util.PersonGenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Collector.Characteristics;

public class ParllelStreamDemo {
    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("Main: Generating parallel streams from different sources");
        System.out.println("********************************************************");

        // Creating a stream from a Collection
        List<Person> persons = PersonGenerator.generatePersonList(10);

/*        System.out.println("********************************************************");
        collectorsJoining(persons);

        System.out.println("********************************************************");
        groupingByConcurrent(persons);

        System.out.println("********************************************************");
        fileReaderStream();

        System.out.println("********************************************************");
        collectorPartitioning(persons);

        System.out.println("********************************************************");
        getConcurrentMap(persons);

        System.out.println("********************************************************");
        forEachOrderedParallelStream();*/

        verifyStreamCondition(persons);
    }

    private static void verifyStreamCondition(List<Person> persons) {

        boolean condition = persons.parallelStream().allMatch(p -> p.getSalary() > 0);
        System.out.printf("Salary > 0: %b\n", condition);

        condition = persons.parallelStream().anyMatch(p -> p.getSalary() > 50000);
        System.out.printf("Any with salary > 50000: %b\n", condition);

        condition = persons.parallelStream().noneMatch(p -> p.getSalary() > 100000);
        System.out.printf("None with salary > 100000: %b\n", condition);

        Person person = persons.parallelStream().findFirst().get();
        System.out.printf("First: %s %s: %d\n", person.getFirstName(), person.getLastName(), person.getSalary());

        persons.parallelStream().sorted((p1, p2) -> {
            return p1.getSalary() - p2.getSalary();
        }).forEachOrdered(person1 -> System.out.println(person1.getFirstName() + ": " + person1.getSalary()));
    }

    private static void forEachOrderedParallelStream() {

        System.out.println("Parallel forEachOrdered() with numbers ");

        List<Double> doubles= DoubleGenerator.generateDoubleList(10, 100);

        doubles.parallelStream().sorted().forEachOrdered(n -> {
            System.out.printf("%f\n",n);
        });
    }

    private static void getConcurrentMap(List<Person> persons) {

        System.out.println("To Concurrent Map");

        ConcurrentMap<String, String> nameMap = persons.parallelStream()
                .peek(person -> System.out.println(person.getFirstName() + " " + person.getLastName()))
                .collect(Collectors.toConcurrentMap(Person::getFirstName, Person::getLastName,
                        (s1, s2) -> s1 + ", " + s2));

        ConcurrentMap<String, List<Person>> nameListMap = persons.parallelStream()
                .peek(person -> System.out.println(person.getFirstName() + " " + person.getLastName()))
                .collect(Collectors.groupingByConcurrent(Person::getFirstName, Collectors.toList()));

        nameListMap.forEach((key, value) -> {
            System.out.printf("%s: %s \n", key, value);
        });
    }

    private static void collectorPartitioning(List<Person> persons) {

        long startTime = System.nanoTime();

        System.out.println("Partitioning By\n");

        Map<Boolean, List<Person>> personsBySalary = persons.parallelStream()
                .collect(Collectors.partitioningBy(p -> p.getSalary() > 50000));

        personsBySalary.keySet().forEach(key -> {
            List<Person> listOfPersons = personsBySalary.get(key);
            System.out.printf("%s: %d \n", key, listOfPersons.size());
        });

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("Duration: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");
    }

    private static void collectorsJoining(List<Person> persons) {

        System.out.println("Joining ...... ");
        System.out.printf("Concurrent: %b\n", Collectors.joining().characteristics().contains(Characteristics.CONCURRENT));

        String message = persons.parallelStream()
                .map(Person::toString)
                .collect(Collectors.joining(","));

        System.out.printf("%s\n", message);
    }

    private static void groupingByConcurrent(List<Person> persons) {

        System.out.println("Grouping By Concurrent");
        Map<String, List<Person>> personsByName = persons.parallelStream()
                .collect(Collectors.groupingByConcurrent(Person::getFirstName));

        personsByName.keySet().forEach(key -> {
            List<Person> listOfPersons = personsByName.get(key);
            System.out.printf("%s: There are %d persons with that name\n", key, listOfPersons.size());
        });
    }

    private static void fileReaderStream() throws FileNotFoundException {

            BufferedReader br = new BufferedReader(new FileReader("data\\nursery.data"));

            List<String> incompleteTasks = br.lines()
                    .filter(line -> line.contains("incomplete"))
                    .peek(System.out::println)
                    .collect(Collectors.toList());

            System.out.printf("Number of incomplete in the file: %d\n\n", incompleteTasks.size());
            System.out.println("********************************************************");
    }
}
