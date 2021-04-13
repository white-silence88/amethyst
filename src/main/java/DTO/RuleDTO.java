package DTO;

public class RuleDTO {
    private final boolean myPublic;
    private final boolean myPrivate;
    private final boolean myGlobal;

    private final boolean otherPublic;
    private final boolean otherPrivate;
    private final boolean otherGlobal;

    public RuleDTO(String rule) {
        Character ALLOWED = '1';

        Character own_Public = rule.charAt(2);
        Character own_Private = rule.charAt(1);
        Character own_Global = rule.charAt(0);

        Character notOwn_Public = rule.charAt(5);
        Character notOwn_Private = rule.charAt(4);
        Character notOwn_Global = rule.charAt(3);

        this.myPublic = own_Public.equals(ALLOWED);
        this.myPrivate = own_Private.equals(ALLOWED);
        this.myGlobal = own_Global.equals(ALLOWED);

        this.otherPublic = notOwn_Public.equals(ALLOWED);
        this.otherPrivate = notOwn_Private.equals(ALLOWED);
        this.otherGlobal = notOwn_Global.equals(ALLOWED);
    }

    public boolean isMyPublic() {
        return myPublic;
    }

    public boolean isMyPrivate() {
        return myPrivate;
    }

    public boolean isMyGlobal() {
        return myGlobal;
    }

    public boolean isOtherPublic() {
        return otherPublic;
    }

    public boolean isOtherPrivate() {
        return otherPrivate;
    }

    public boolean isOtherGlobal() {
        return otherGlobal;
    }
}
