package ru.testservice.serviceapp.interceptors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.IStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MoreCoursesInterceptor extends HandlerInterceptorAdapter {

    private final String[] urls = new String[]{"/course/", "/section/"};
    private final CourseService cs;
    private final IStorageService IStorageService;

    public MoreCoursesInterceptor(CourseService cs, IStorageService IStorageService) {
        this.cs = cs;
        this.IStorageService = IStorageService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With")) && modelAndView != null) {
            String viewName = modelAndView.getViewName();
            if (viewName !=null && !viewName.contains("/redirect"))
                modelAndView.addObject("viewName",  viewName);
            else {
                modelAndView.addObject("viewName",  "null");
            }
//
        }

        super.postHandle(request, response, handler, modelAndView);
    }
}
