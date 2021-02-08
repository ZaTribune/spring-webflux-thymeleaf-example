package zatribune.spring.kitchenmaster.services;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import zatribune.spring.kitchenmaster.commands.UnitMeasureCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitMeasureServiceImplTest {

    @Mock
    UnitMeasureService unitMeasureService;

    String s1="ounce";
    String s2="dash";
    String s3="cup";

    @BeforeEach
    void setUp() {
    }



    @Test
    void getAllUnitMeasures() {
        UnitMeasureCommand um1 = new UnitMeasureCommand();
        um1.setId(new ObjectId().toString());
        um1.setDescription(s1);
        UnitMeasureCommand um2 = new UnitMeasureCommand();
        um2.setId(new ObjectId().toString());
        um2.setDescription(s2);
        UnitMeasureCommand um3 = new UnitMeasureCommand();
        um3.setId(new ObjectId().toString());
        um3.setDescription(s3);
        when(unitMeasureService.getAllUnitMeasures()).thenReturn(Flux.just(um1,um2,um3));
        Flux<UnitMeasureCommand>result=unitMeasureService.getAllUnitMeasures();
        verify(unitMeasureService,times(1)).getAllUnitMeasures();
        assertEquals(3L,result.count().block());
        System.out.println(result.count().block());
    }
}