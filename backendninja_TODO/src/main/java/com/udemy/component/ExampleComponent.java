package com.udemy.component;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.udemy.repository.CourseJPARepository;

@Component("exampleComponent")
public class ExampleComponent {
    
    private static final Log LOG = LogFactory.getLog(ExampleComponent.class);
    
    @Autowired
    @Qualifier("courseJpaRepository")
    private CourseJPARepository courseJpaRepository;
    
    public void sayHello(){
        LOG.info("HELLO FROM EXAMPLE COMPONENT");
    }
}
