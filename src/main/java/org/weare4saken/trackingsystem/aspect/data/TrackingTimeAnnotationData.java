package org.weare4saken.trackingsystem.aspect.data;

import java.lang.annotation.Annotation;

public interface TrackingTimeAnnotationData {

    String methodCategory();

    Boolean ignoreExceptions();

    Annotation getAnnotationName();
}
