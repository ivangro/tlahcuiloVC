package subgraph;

/**
 * Determines the different types of relations that can be generated between two nodes<br>
 * H: Horizontal<br>
 * V: Vertical<br>
 * R: Radial<br>
 * T: Translation<br>
 * M: Mirroring<br>
 * AS: Asymmetry<br>
 * S: Symmetry<br>
 * UB: Unbalanced<br>
 * B: Balance<br>
 * New relations between elements:<br>
 * L: Line segment<br>
 * SR: Surrounded<br>
 */
public enum ElementRelationType {AS, HTS, VTS, RS, HMS, VMS, UB, HB, VB, NEW_ELEM,
                                 HL, VL, SR};