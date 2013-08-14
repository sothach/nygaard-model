package org.seefin.nygaard.model.parties;

import org.junit.Test;
import org.seefin.nygaard.model.identifiers.ISO9362;
import org.seefin.nygaard.model.identifiers.Identity;
import org.seefin.nygaard.model.parties.FinancialInstitution;
import org.seefin.nygaard.model.parties.OrganizationalUnit;


/**
 * @author phillipsr
 *
 */
public class Organizations
{
	@Test
	public void
	testBank()
	{
		Identity bankId = ISO9362.valueOf ( "ANGOIE2DXXX");
		FinancialInstitution bank = new FinancialInstitution ( bankId , "AngloIrish");
		Identity branchId = ISO9362.valueOf ( "ANGOIE2DDET");
		OrganizationalUnit branch = new OrganizationalUnit ( branchId, bank, "AgencyServices");

		System.out.println ( "Branch=" + branch);
	}
}
