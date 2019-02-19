package ac;

import java.util.ArrayList;

/**
 * @author vx5
 *
 *         Intermediary class that allows an Autocorrect implementation to
 *         handle more than one Autocorrecting operator (such as if we want to
 *         have two entirely different autocorrects on the same implementation,
 *         such as in the same FreeMarker template).
 *
 */
public class AcCoordinator {
  // Stores list of AcOperators present for this project
  private ArrayList<AcOperator> ops;

  /**
   * Constructor that simply initializes the list of AcOperators
   */
  public AcCoordinator() {
    ops = new ArrayList<AcOperator>();
  }

  /**
   * Adds an AcOperator to this AcCoordinator
   *
   * @param op AcOperator to be added to this AcCoordinator
   */
  public void addOp(AcOperator op) {
    ops.add(op);
  }

  /**
   * Returns a particular AcOperator from this AcCoordinator instance
   *
   * @param opNum Integer that identifies which AcOperator in the list of
   *              AcOperators to return
   * @return the desired AcOperator
   */
  public AcOperator getOp(int opNum) {
    return ops.get(opNum);
  }
}
