package bbw.ch.FairwayEcoBackend.model;

/**
 * Enum representing the condition of a recovered golf ball.
 */
public enum BallCondition {
  MINT("Mint - Like new, no visible marks"),
  GRADE_A("Grade A - Minimal wear, excellent condition"),
  GRADE_B("Grade B - Light scuffs, good condition"),
  GRADE_C("Grade C - Visible wear, practice quality"),
  PRACTICE("Practice - Heavy wear, driving range quality");

  private final String description;

  BallCondition(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
