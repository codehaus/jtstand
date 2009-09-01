
import demo.Foo;
import com.jtstand.StepInterface;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author albert_kurucz
 */
public class InitClass2 implements Runnable {

    StepInterface step;

    public InitClass2(StepInterface step) {
        this.step = step;
    }

    @Override
    public void run() {
        Foo f = new Foo();
        f.hello();
        f.fello();
        step.setValue(f.getResult());
    }
}
