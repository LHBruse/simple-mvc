package lhbruse.controller;

import lhbruse.annotation.*;
import lhbruse.exception.TestException;
import lhbruse.http.RequestMethod;
import lhbruse.vo.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * @author: LHB
 * @createDate: 2021/10/21
 * @version: 1.0
 */
@ControllerAdvice
@Controller
@RequestMapping(path = "/test")
public class DispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("DispatcherController.dispatch: name=>" + name);
        model.addAttribute("name", name);
        return "/asd.jsp";
    }

    @RequestMapping(path = "/dispatch2", method = RequestMethod.GET)
    public String dispatch2(@RequestParam(name = "name") String name) {
        System.out.println("DispatcherController.dispatch2: name=>" + name);
        
        throw new TestException("test exception", name);
    }

    @ResponseBody
    //@ExceptionHandler({TestException.class})
    public ApiResponse exceptionHandler(TestException ex) {
        System.out.println("exception message:" + ex.getMessage());
        return new ApiResponse(200, "exception handle complete", ex.getName());
    }


}
