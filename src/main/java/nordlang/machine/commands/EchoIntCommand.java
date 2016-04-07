package nordlang.machine.commands;

import nordlang.exceptions.RunException;
import nordlang.machine.MachineState;

public class EchoIntCommand extends Command {

    @Override
    public void execute(MachineState state) throws RunException {
        System.out.print(state.getValueStack().pop());
        state.incrementCmdIndex();
    }

    @Override
    public String toString() {
        return "ECHO_INT";
    }
}
