package samples.stream;

import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author gentjan kolicaj
 * @since 3/30/26 3:16 PM
 *
 */
@Slf4j
public class StreamSample0 {

  public static void main(String[] args) {

    //Print all numbers in stream
    getInts().forEach(number -> log.info("All {}", number));

    //Print number from that are less than 5
    getInts().filter(number -> number < 5).forEach(number -> log.info("Less than 5, {}", number));

    //Print number 4 bigger than 5, skip first 3
    getInts().filter(number -> number > 5)
        .skip(3)
        .limit(4)
        .forEach(number -> log.info("4 bigger than 5, skip first 3: {}", number));

    //Print 5 numbers bigger than 5 if nothing print -1
    log.info("Print first bigger than 5 or -1: {}", getInts().filter(number -> number > 5)
        .findFirst().orElse(-1));

    //Print all usernames
    getUsers().map(User::getUsername).forEach(uname -> log.info("User uname: {}", uname));

    //Print all username in where user.id= a and a > 4
    getInts().filter(i -> i > 4)
        .flatMap(integer -> getUsers().filter(user -> user.getId() == integer))
        .forEach(user -> log.info("User flatMap: {}", user));
  }

  public static Stream<Integer> getInts() {
    return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17);
  }

  public static Stream<User> getUsers() {
    return Stream.of(
        new User(0, "uname0"),
        new User(1, "uname1"),
        new User(2, "uname2"),
        new User(3, "uname3"),
        new User(4, "uname4"),
        new User(5, "uname5"),
        new User(6, "uname6")
    );
  }

  @ToString
  @Getter
  @AllArgsConstructor
  public static class User {

    private int id;
    private String username;
  }

}
