package ac;

import java.util.LinkedList;

public class AcCoordinator {
  // Stores list of AcOperators present for this project
  private LinkedList<AcOperator> ops;

  public AcCoordinator() {
  }

  public void addOp(AcOperator op) {
    ops.add(op);
  }

  public AcOperator getOp(int opNum) {
    return ops.get(opNum);
  }
}
