package net.twasi.core.database.repositories;

import net.twasi.core.database.lib.Repository;
import net.twasi.core.database.models.BetaCode;

public class BetaCodeRepository extends Repository<BetaCode> {
    /**
     * Return a BetaCode object by the corresponding code
     * @param betaCode the code to look for
     * @return the BetaCode object, or null if the code is not valid
     */
    public BetaCode getByCode(String betaCode) {
        return query().field("betaCode").equal(betaCode).get();
    }
}
