package ru.testservice.serviceapp.interceptors;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriBuilder;
import ru.testservice.serviceapp.service.CourseService;
import ru.testservice.serviceapp.service.IStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;

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
            if (viewName != null && !viewName.contains("/redirect"))
                modelAndView.addObject("viewName", viewName);
            else {
                modelAndView.addObject("viewName", "null");
            }
//
        }

        StringBuilder vkShare = new StringBuilder("https://vk.com/share.php?url=");
        StringBuilder fbShare = new StringBuilder("https://www.facebook.com/sharer.php?src=");
        StringBuilder okShare = new StringBuilder("https://connect.ok.ru/offer?url=");
        String reqURl = request.getRequestURL().append('?').append(request.getQueryString()).toString();
        StringBuilder common = new StringBuilder().append(URLEncoder.encode(reqURl, "UTF-8")).append("&title=").append(URLEncoder.encode(modelAndView.getModelMap().get("htmlTitle").toString(),"UTF-8"));;
        vkShare.append(common.toString());
        fbShare.append(common.toString());
        okShare.append(common.toString());
        modelAndView.addObject("vkShare", vkShare.toString());
        modelAndView.addObject("fbShare", fbShare.toString());
        modelAndView.addObject("okShare", okShare.toString());
        super.postHandle(request, response, handler, modelAndView);
    }
}
