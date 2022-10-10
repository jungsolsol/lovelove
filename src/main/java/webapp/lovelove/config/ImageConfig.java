//package webapp.lovelove.config;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class ImageConfig implements WebMvcConfigurer {
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("resources/**")
//                .addResourceLocations("classpath:/static/js/")
//                .setCacheControl(CacheControl.noCache().cachePrivate());
//        registry.addResourceHandler("resources/**")
//                .addResourceLocations("classpath:/static/css/")
//                .setCachePeriod(60 * 60 * 24 * 365);
//    }
//}