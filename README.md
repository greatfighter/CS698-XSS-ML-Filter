CS698 mini research project source codes
This project is not focused on how to train a good model preventing XSS, it is more likely to create a novel structure to plug in different services to do XSS filtering.

Filtering the Cross-site scripting with local and server api is the main functionality.

__Cross-site scripting (XSS)__ is a type of computer security vulnerability typically found in web applications. XSS enables attackers to inject client-side scripts into web pages viewed by other users. A cross-site scripting vulnerability may be used by attackers to bypass access controls such as the same-origin policy[..read more...](https://en.wikipedia.org/wiki/Cross-site_scripting)


__XssRequestFilter__ is a spring based lib to filter the cross-site scripting in your @Controller /@RestController request (current version have xss filter support for form data and json request) just using a simple @XssFilter annotation . Also, it's easy to customized with own custom rules for filter xss request.


Use @XssFilter annotation on your controller methods where you wish to filter  Cross-site scripting.
It will remove all xss from request parameter. Also use  `@ComponentScan("com.xss.filter")` in your one of configuration class for active the autoconfiguration for XssRequestFilter.

XSS filtering Regex is stored in folder util, which contains basic filter case and api's generated regex pattern through language model either from local ML service or public LLM like OpenAI.

The local ML servie api and OpenAI api are examples to show the achieveability of this structure.

The structure is not thoroughly tested due to time limitation. Please check the structure before using.

example:
```
@Configuration
@ComponentScan("com.xss.filter")
public class Config {
}

````


```

@RestController
public class Api {

  /**
   * Test with Content-Type: application/x-www-form-urlencoded
   * Sample Test Curl Request:
   * curl --location --request POST 'http://localhost:8080/test-with-model-attribute' \
   * --header 'Content-Type: application/x-www-form-urlencoded' \
   * --data-urlencode 'name=data <div id=\"demo\">Inject Html</div>'
   *
   *  Response :
   *  {
   *     "name": "data "
   * }
   *
   * you can see in response sample <div id=\"demo\">Inject Html</div> removed by XssFilter
   * */
  @XssFilter
  @RequestMapping(value = "/test-with-model-attribute", method = RequestMethod.POST)
  public Person save(@ModelAttribute Person person) {
    return new Person(person.getName());
  }

  /**
   * Test with Content-Type: application/json
   *
   * Curl request :
   * curl --location --request POST 'http://localhost:8080/test-with-request-body' \
   * --header 'Content-Type: application/json' \
   * --data-raw '{
   *     "name" : "gf <div id=\"demo\">Inject Html</div>"
   * }'
   *
   * Response of API :
   * {
   *     "name": "gf "
   * }
   *
   * Here in response result :  <div id=\"demo\">Inject Html</div> removed by XssFilter
   * */
  @XssFilter
  @PostMapping("/test-with-request-body")
  public Person personNameProcess(@RequestBody Person request,
      HttpServletRequest httpServletRequest) {
    return new Person(request.getName());
  }


  public static class Person {

    private String name;

    public Person() {
    }

    public Person(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

}

