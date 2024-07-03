Filter the Cross-site scripting

__Cross-site scripting (XSS)__ is a type of computer security vulnerability typically found in web applications. XSS enables attackers to inject client-side scripts into web pages viewed by other users. A cross-site scripting vulnerability may be used by attackers to bypass access controls such as the same-origin policy[..read more...](https://en.wikipedia.org/wiki/Cross-site_scripting)


__XssRequestFilter__ is a spring based lib to filter the cross-site scripting in your @Controller /@RestController request (current version have xss filter support for form data and json request) just using a simple @XssFilter annotation . Also, it's easy to customized with own custom rules for filter xss request.


Use @XssFilter annotation on your controller methods where you wish to filter  Cross-site scripting.
It will remove all xss from request parameter. Also use  `@ComponentScan("com.xss.filter")` in your one of configuration class for active the autoconfiguration for XssRequestFilter.

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


```


Note : `@XssFilter` bound with Url Patterns so it can process matched url with different type of request method.
 
 
## Following regex pattern (most of them are case-insensitive Pattern) are used for filter xss value :

* `(<input(.*?)></input>|<input(.*)/>)`

* `<span(.*?)</span>`

* `<div(.*)</div>`

* `<style>(.*?)</style>`

* `<script>(.*?)</script>`

* `javascript:`

* `</script>`

* `<script(.*?)>`

* `src[\r\n]*=[\r\n]*\\\'(.*?)\\\'`

* `eval\((.*?)\)`

* `expression\((.*?)\)`

* `vbscript:`

* `onload(.*?)=`

[XSS Filter Evasion Cheat Sheet](https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet)

This library employs a default blacklist approach to identify known malicious inputs and block them. While this method forms a part of the security strategy, it is not comprehensive on its own. Attackers frequently discover ways to evade blacklist filters, employing diverse encoding techniques or devising new attack vectors not covered by the blacklist. For every identified case, there exist countless scenarios, making it crucial to have a more adaptable defense.

To address this challenge, the library provides an interface called **XssAttackService**. Developers utilizing this library can create their custom implementations of this interface, enabling them to employ a whitelist approach.
This approach offers a more robust defense against XSS attacks. Developers can tailor their implementation according to the specific requirements of their APIs or business data, ensuring a personalized and effective security barrier. The flexibility of the whitelist approach empowers developers to safeguard their applications effectively, mitigating the risks posed by evolving XSS attack techniques.

## Create your own custom logic for filter xss request:
If the predefined blacklist patterns don't meet your security needs, you have the flexibility to establish your own custom whitelist approach or even combine whitelisted and blacklisted filters for XSS patterns. To facilitate this customization, the XssRequestFilter offers robust support. It allows you to implement tailored logic, empowering you to filter XSS requests according to your specific requirements.
This flexibility ensures that you can create a security framework that aligns precisely with your application's unique demands, providing a higher level of protection against XSS attacks.

By default this framework use [DefaultRansackXssImpl](https://github.com/techguy-bhushan/XssRequestFilters/blob/master/src/main/java/com/xss/filter/service/impl/DefaultRansackXssServiceImpl.java)
service for filter the xss request, this service implemented **XssAttackService** interface.

So for your custom logic for filter xss request you just need following steps:
1. Create a class which will XssAttackService the RansackXss interface.
2. Implement the `String ransack(String value);` method. (value can be ServletRequest headers/parameters/json payload form where client can inject the xss, you need to perform the filter on this value, you can take a reference of DefaultRansackXssServiceImpl class)
3. Create a bean of this class 

done... Now instead of DefaultRansackXssServiceImpl, XssAttackService will use your class implementation rules for filter xss

### Supported Request content type : (version >= 1.1.0)
* application/x-www-form-urlencoded
* application/json

### Xss Version Support with Spring-boot 
| Springboot vesrion | xss.filter version support |
|--------------------|----------------------------|
| SpringBoot 2.\*.*  | 1.1.0                      |
| SpringBoot 3.\*.*  | 2.0.0                      |

## Download it from here  
 
 * Gradle/Grails

  
## Here are some useful classes used in XssRequestFilter

### XssFiltersConfiguration :
 This Component will search all the url's which action are annotated with `@XssFilter` (collect the list of urls, which will be pick by CustomXssFilter )
 
### CustomXssFilter:
 This filter will only work for request which action have annotated `@XssFilter` (with help of XssFiltersConfiguration)
 
### CaptureRequestWrapper :
 This class is responsible for filter the XSS in request you can add or remove the XSS handling logic in #stripXSS method  in CaptureRequestWrapper,  `CustomXssFilter` use this class for remove xss in request.
 
### FilterConfig : 
  This component will register CustomXssFilter if there will any @XssFilter annotation used in url mapping, if there will no @XssFilter used in application then `CustomXssFilter` will disable.

### ServletInputStreamXssFilterProvider
 This component has list of ServletInputStreamXssFilterProvider which are responsible for process ServletInputStream based on supported content types, you can customize this component based on your own requirement.
  
  

 
