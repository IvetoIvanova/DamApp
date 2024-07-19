package bg.softuni.damapp.validation.annotations;

import bg.softuni.damapp.model.entity.UUIDSequenceGenerator;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@ValueGenerationType(generatedBy = UUIDSequenceGenerator.class)
public @interface UUIDSequence {

}
