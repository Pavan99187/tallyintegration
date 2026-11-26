package com.example.tallyintegration.xmlconstants;

public class TallyXmlConstants {

    public static final String TALLY_LEDGER_CREATION  =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ENVELOPE>\n" +
                    "  <HEADER>\n" +
                    "    <TALLYREQUEST>Import Data</TALLYREQUEST>\n" +
                    "  </HEADER>\n" +
                    "  <BODY>\n" +
                    "    <IMPORTDATA>\n" +
                    "      <REQUESTDESC>\n" +
                    "        <REPORTNAME>All Masters</REPORTNAME>\n" +
                    "        <STATICVARIABLES>\n" +
                    "          <SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>\n" +
                    "        </STATICVARIABLES>\n" +
                    "      </REQUESTDESC>\n" +
                    "      <REQUESTDATA>\n" +
                    "        <TALLYMESSAGE xmlns:UDF=\"TallyUDF\">\n" +
                    "          <LEDGER NAME=\"{ledgerName}\" ACTION=\"Create\">\n" +
                    "            <NAME>{ledgerName}</NAME>\n" +
                    "            <PARENT>{ledgerParent}</PARENT>\n" +
                    "            <ISBILLWISEON>Yes</ISBILLWISEON>\n" +
                    "            <BILLCREDITPERIOD>{creditPeriod}</BILLCREDITPERIOD>\n" +
                    "            <OPENINGBALANCE>{openingBalance}</OPENINGBALANCE>\n" +
                    "            <PRIORSTATENAME>{ledgerStateName}</PRIORSTATENAME>\n" +
                    "            <COUNTRYOFRESIDENCE>{country}</COUNTRYOFRESIDENCE>\n" +
                    "            <LEDGSTREGDETAILS.LIST>\n" +
                    "              <APPLICABLEFROM>{applicableFrom}</APPLICABLEFROM>\n" +
                    "              <GSTREGISTRATIONTYPE>{gstRegistrationType}</GSTREGISTRATIONTYPE>\n" +
                    "              <PLACEOFSUPPLY>{state}</PLACEOFSUPPLY>\n" +
                    "              <GSTIN>{gstin}</GSTIN>\n"  +
                    "              <ISOTHTERRITORYASSESSEE>No</ISOTHTERRITORYASSESSEE>\n" +
                    "              <CONSIDERPURCHASEFOREXPORT>No</CONSIDERPURCHASEFOREXPORT>\n" +
                    "              <ISTRANSPORTER>No</ISTRANSPORTER>\n" +
                    "              <ISCOMMONPARTY>No</ISCOMMONPARTY>\n" +
                    "            </LEDGSTREGDETAILS.LIST>\n" +
                    "            <LEDMAILINGDETAILS.LIST>\n" +
                    "              <ADDRESS.LIST TYPE=\"String\">\n" +
                    "                <ADDRESS>{address1}</ADDRESS>\n" +
                    "               <ADDRESS>{address2}</ADDRESS>\n" +
                    "                <ADDRESS>{address3}</ADDRESS>\n" +
                    "                <ADDRESS>{address4}</ADDRESS>\n"  +
                    "              </ADDRESS.LIST>\n" +
                    "              <APPLICABLEFROM>{applicableFrom}</APPLICABLEFROM>\n" +
                    "              <PINCODE>{pincode}</PINCODE>\n" +
                    "              <MAILINGNAME>{ledgerName}</MAILINGNAME>\n" +
                    "              <STATE>{ledgerStateName}</STATE>\n" +
                    "              <COUNTRY>{country}</COUNTRY>\n" +
                    "            </LEDMAILINGDETAILS.LIST>\n" +
                    "            <LEDGERPHONE>{phoneNumber}</LEDGERPHONE>\n" +
                    "            <LEDGERMOBILE>{phoneNumber}</LEDGERMOBILE>\n" +
                    "            <EMAIL>{email}</EMAIL>\n" +
                    "            <INCOMETAXNUMBER>{panNumber}</INCOMETAXNUMBER>\n" +
                    "            <GSTREGISTRATIONTYPE>{gstRegistrationType}</GSTREGISTRATIONTYPE>\n" +
                    "            <PARTYGSTIN>{gstin}</PARTYGSTIN>\n" +
                    "             <LANGUAGENAME.LIST>\n" +
                    "            <NAME.LIST TYPE=\"String\">\n" +
                    "            <NAME>{ledgerName}</NAME>\n" +
                    "            <NAME>{uniqueId}</NAME> <!-- This is your Alias -->\n" +
                    "             </NAME.LIST>\n" +
                    "    <LANGUAGEID>1033</LANGUAGEID>\n" +
                    "</LANGUAGENAME.LIST>\n" +
                    "          </LEDGER>\n" +
                    "        </TALLYMESSAGE>\n" +
                    "      </REQUESTDATA>\n" +
                    "    </IMPORTDATA>\n" +
                    "  </BODY>\n" +
                    "</ENVELOPE>";



    public static final String TALLY_SALES_INVOICE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ENVELOPE>\n" +
                    "  <HEADER>\n" +
                    "    <TALLYREQUEST>Import Data</TALLYREQUEST>\n" +
                    "  </HEADER>\n" +
                    "  <BODY>\n" +
                    "    <IMPORTDATA>\n" +
                    "      <REQUESTDESC>\n" +
                    "        <REPORTNAME>Vouchers</REPORTNAME>\n" +
                    "        <STATICVARIABLES>\n" +
                    "          <SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>\n" +
                    "        </STATICVARIABLES>\n" +
                    "      </REQUESTDESC>\n" +
                    "      <REQUESTDATA>\n" +
                    "        <TALLYMESSAGE xmlns:UDF=\"TallyUDF\">\n" +
                    "          <VOUCHER VCHTYPE=\"Sales\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">\n" +
                    "            <DATE>{invoiceDate}</DATE>\n" +
                    "            <EFFECTIVEDATE>{invoiceDate}</EFFECTIVEDATE>\n" +
                    "            <VOUCHERTYPENAME>Sales</VOUCHERTYPENAME>\n" +
                    "            <VOUCHERNUMBER>{invoiceNumber}</VOUCHERNUMBER>\n" +
                    "            <PARTYLEDGERNAME>{customerLedger}</PARTYLEDGERNAME>\n" +
                    "            <PARTYNAME>{customerLedger}</PARTYNAME>\n" +
                    "            <BASICBUYERNAME>{customerLedger}</BASICBUYERNAME>\n" +
                    "            <STATENAME>{placeOfSupply}</STATENAME>\n" +
                    "            <COUNTRYOFRESIDENCE>India</COUNTRYOFRESIDENCE>\n" +
                    "            <PLACEOFSUPPLY>{placeOfSupply}</PLACEOFSUPPLY>\n" +
                    "            {partyGstTags}\n" +
                    "            <PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>\n" +
                    "            <VCHENTRYMODE>Item Invoice</VCHENTRYMODE>\n" +
                    "            <ISINVOICE>Yes</ISINVOICE>\n" +
                    "            {inventoryEntries}\n" +
                    "            <LEDGERENTRIES.LIST>\n" +
                    "              <LEDGERNAME>{customerLedger}</LEDGERNAME>\n" +
                    "              <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>\n" +
                    "              <ISPARTYLEDGER>Yes</ISPARTYLEDGER>\n" +
                    "              <AMOUNT>-{totalAmount}</AMOUNT>\n" +
                    "              <BILLALLOCATIONS.LIST>\n" +
                    "                <NAME>{invoiceNumber}</NAME>\n" +
                    "                <BILLTYPE>New Ref</BILLTYPE>\n" +
                    "                <AMOUNT>-{totalAmount}</AMOUNT>\n" +
                    "              </BILLALLOCATIONS.LIST>\n" +
                    "            </LEDGERENTRIES.LIST>\n" +
                    "            {taxEntries}\n" +
                    "          </VOUCHER>\n" +
                    "        </TALLYMESSAGE>\n" +
                    "      </REQUESTDATA>\n" +
                    "    </IMPORTDATA>\n" +
                    "  </BODY>\n" +
                    "</ENVELOPE>";



    // Replace TALLY_PURCHASE_INVOICE inside TallyXmlConstants with this constant.

    public static final String TALLY_PURCHASE_INVOICE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ENVELOPE>\n" +
                    "  <HEADER>\n" +
                    "    <TALLYREQUEST>Import Data</TALLYREQUEST>\n" +
                    "  </HEADER>\n" +
                    "  <BODY>\n" +
                    "    <IMPORTDATA>\n" +
                    "      <REQUESTDESC>\n" +
                    "        <REPORTNAME>Vouchers</REPORTNAME>\n" +
                    "        <STATICVARIABLES>\n" +
                    "          <SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>\n" +
                    "        </STATICVARIABLES>\n" +
                    "      </REQUESTDESC>\n" +
                    "      <REQUESTDATA>\n" +
                    "        <TALLYMESSAGE xmlns:UDF=\"TallyUDF\">\n" +
                    "          <VOUCHER VCHTYPE=\"Purchase\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">\n" +
                    "            <DATE>{invoiceDate}</DATE>\n" +
                    "            <REFERENCEDATE>{invoiceDate}</REFERENCEDATE>\n" +
                    "            <EFFECTIVEDATE>{invoiceDate}</EFFECTIVEDATE>\n" +
                    "            <VOUCHERTYPENAME>Purchase</VOUCHERTYPENAME>\n" +
                    "            <VOUCHERNUMBER>{invoiceNumber}</VOUCHERNUMBER>\n" +
                    "           <REFERENCE>{invoiceNumber}</REFERENCE>\n" +
                    "            {referenceTags}\n" +
                    "            <PARTYLEDGERNAME>{supplierLedger}</PARTYLEDGERNAME>\n" +
                    "            <PARTYNAME>{supplierLedger}</PARTYNAME>\n" +
                    "            <BASICBASEPARTYNAME>{supplierLedger}</BASICBASEPARTYNAME>\n" +
                    "            <BASICBUYERNAME>{companyName}</BASICBUYERNAME>\n" +
                    "            <CONSIGNEEMAILINGNAME>{companyName}</CONSIGNEEMAILINGNAME>\n" +
                    "            <CONSIGNEESTATENAME>{placeOfSupply}</CONSIGNEESTATENAME>\n" +
                    "            <CONSIGNEECOUNTRYNAME>India</CONSIGNEECOUNTRYNAME>\n" +
                    "            <COUNTRYOFRESIDENCE>India</COUNTRYOFRESIDENCE>\n" +
                    "            {supplierStateTags}\n" +
                    "            <PLACEOFSUPPLY>{placeOfSupply}</PLACEOFSUPPLY>\n" +
                    "            {companyStateTags}\n" +
                    "            {partyGstTags}\n" +
                    "            <PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>\n" +
                    "            <VCHENTRYMODE>Item Invoice</VCHENTRYMODE>\n" +
                    "            <ISINVOICE>Yes</ISINVOICE>\n" +
                    "            {inventoryEntries}\n" +
                    "            <LEDGERENTRIES.LIST>\n" +
                    "              <LEDGERNAME>{supplierLedger}</LEDGERNAME>\n" +
                    "              <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>\n" +
                    "              <ISPARTYLEDGER>Yes</ISPARTYLEDGER>\n" +
                    "              <AMOUNT>{totalAmount}</AMOUNT>\n" +
                    "              <BILLALLOCATIONS.LIST>\n" +
                    "                <NAME>{billRef}</NAME>\n" +
                    "                <BILLTYPE>New Ref</BILLTYPE>\n" +
                    "                <AMOUNT>{totalAmount}</AMOUNT>\n" +
                    "              </BILLALLOCATIONS.LIST>\n" +
                    "            </LEDGERENTRIES.LIST>\n" +
                    "            {taxEntries}\n" +
                    "          </VOUCHER>\n" +
                    "        </TALLYMESSAGE>\n" +
                    "      </REQUESTDATA>\n" +
                    "    </IMPORTDATA>\n" +
                    "  </BODY>\n" +
                    "</ENVELOPE>";



//    public static final String TALLY_GRN =
//            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                    "<ENVELOPE>\n" +
//                    "  <HEADER>\n" +
//                    "    <TALLYREQUEST>Import Data</TALLYREQUEST>\n" +
//                    "  </HEADER>\n" +
//                    "  <BODY>\n" +
//                    "    <IMPORTDATA>\n" +
//                    "      <REQUESTDESC>\n" +
//                    "        <REPORTNAME>Vouchers</REPORTNAME>\n" +
//                    "        <STATICVARIABLES>\n" +
//                    "          <SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>\n" +
//                    "        </STATICVARIABLES>\n" +
//                    "      </REQUESTDESC>\n" +
//                    "      <REQUESTDATA>\n" +
//                    "        <TALLYMESSAGE xmlns:UDF=\"TallyUDF\">\n" +
//                    "          <VOUCHER VCHTYPE=\"Receipt Note\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">\n" +
//                    "            <DATE>{grnDate}</DATE>\n" +
//                    "            <VCHSTATUSDATE>{grnDate}</VCHSTATUSDATE>\n" +
//                    "            <EFFECTIVEDATE>{grnDate}</EFFECTIVEDATE>\n" +
//                    "            <VOUCHERTYPENAME>Receipt Note</VOUCHERTYPENAME>\n" +
//                    "            <VOUCHERNUMBER>{grnNumber}</VOUCHERNUMBER>\n" +
//                    "            <REFERENCE>{purchaseOrderNumber}</REFERENCE>\n" +
//                    "            <PARTYNAME>{supplierLedgerName}</PARTYNAME>\n" +
//                    "            <PARTYLEDGERNAME>{supplierLedgerName}</PARTYLEDGERNAME>\n" +
//                    "            <PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>\n" +
//                    "            <ISINVOICE>No</ISINVOICE>\n" +
//                    "            {inventoryEntries}\n" +
//                    "            <NARRATION>{narration}</NARRATION>\n" +
//                    "          </VOUCHER>\n" +
//                    "        </TALLYMESSAGE>\n" +
//                    "      </REQUESTDATA>\n" +
//                    "    </IMPORTDATA>\n" +
//                    "  </BODY>\n" +
//                    "</ENVELOPE>";


    public static final String TALLY_GRN =
            "<ENVELOPE>" +
                    "<HEADER>" +
                    "<TALLYREQUEST>Import Data</TALLYREQUEST>" +
                    "</HEADER>" +
                    "<BODY>" +
                    "<IMPORTDATA>" +
                    "<REQUESTDESC>" +
                    "<REPORTNAME>Vouchers</REPORTNAME>" +
                    "<STATICVARIABLES>" +
                    "<SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>" +
                    "</STATICVARIABLES>" +
                    "</REQUESTDESC>" +
                    "<REQUESTDATA>" +
                    "<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">" +
                    "<VOUCHER VCHTYPE=\"Receipt Note\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">" +

                    "<DATE>{grnDate}</DATE>" +
                    "<VCHSTATUSDATE>{grnDate}</VCHSTATUSDATE>" +
                    "<EFFECTIVEDATE>{grnDate}</EFFECTIVEDATE>" +

                    "<VOUCHERTYPENAME>Receipt Note</VOUCHERTYPENAME>" +
                    "<VOUCHERNUMBER>{grnNumber}</VOUCHERNUMBER>" +

                    "<PARTYNAME>{supplierLedgerName}</PARTYNAME>" +
                    "<PARTYLEDGERNAME>{supplierLedgerName}</PARTYLEDGERNAME>" +
                    "<BASICBUYERNAME>{companyName}</BASICBUYERNAME>" +
                    "<BASICBASEPARTYNAME>{supplierLedgerName}</BASICBASEPARTYNAME>" +

                    "<REFERENCE>{purchaseOrderNumber}</REFERENCE>" +
                    "<PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>" +
                    "<VCHSTATUSVOUCHERTYPE>Receipt Note</VCHSTATUSVOUCHERTYPE>" +
                    "<NUMBERINGSTYLE>Auto Retain</NUMBERINGSTYLE>" +
                    "<ISINVOICE>No</ISINVOICE>" +

                    "{inventoryEntries}" +

                    "<NARRATION>{narration}</NARRATION>" +

                    "</VOUCHER>" +
                    "</TALLYMESSAGE>" +
                    "</REQUESTDATA>" +
                    "</IMPORTDATA>" +
                    "</BODY>" +
                    "</ENVELOPE>";



    public static final String TALLY_REJECTIONS_OUT =
            "<ENVELOPE>" +
                    "<HEADER>" +
                    "<TALLYREQUEST>Import Data</TALLYREQUEST>" +
                    "</HEADER>" +
                    "<BODY>" +
                    "<IMPORTDATA>" +
                    "<REQUESTDESC>" +
                    "<REPORTNAME>Vouchers</REPORTNAME>" +
                    "<STATICVARIABLES>" +
                    "<SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>" +
                    "</STATICVARIABLES>" +
                    "</REQUESTDESC>" +
                    "<REQUESTDATA>" +
                    "<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">" +

                    "<VOUCHER VCHTYPE=\"Rejections Out\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">" +

                    "<DATE>{returnDate}</DATE>" +
                    "<VCHSTATUSDATE>{returnDate}</VCHSTATUSDATE>" +
                    "<EFFECTIVEDATE>{returnDate}</EFFECTIVEDATE>" +

                    "<VOUCHERTYPENAME>Rejections Out</VOUCHERTYPENAME>" +
                    "<PARTYNAME>{supplierLedgerName}</PARTYNAME>" +
                    "<PARTYLEDGERNAME>{supplierLedgerName}</PARTYLEDGERNAME>" +

                    "<VOUCHERNUMBER>{returnNumber}</VOUCHERNUMBER>" +
                    "<NUMBERINGSTYLE>Auto Retain</NUMBERINGSTYLE>" +

                    "<PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>" +
                    "<VCHSTATUSVOUCHERTYPE>Rejections Out</VCHSTATUSVOUCHERTYPE>" +

                    "<USETRACKINGNUMBER>No</USETRACKINGNUMBER>" +
                    "<ISINVOICE>No</ISINVOICE>" +

                    "{inventoryEntries}" +

                    "<NARRATION>{narration}</NARRATION>" +

                    "</VOUCHER>" +
                    "</TALLYMESSAGE>" +
                    "</REQUESTDATA>" +
                    "</IMPORTDATA>" +
                    "</BODY>" +
                    "</ENVELOPE>";



    public static final String TALLY_PURCHASE_ORDER = """
            <?xml version="1.0" encoding="UTF-8"?>
            <ENVELOPE>
              <HEADER>
                <TALLYREQUEST>Import Data</TALLYREQUEST>
              </HEADER>
              <BODY>
                <IMPORTDATA>
                  <REQUESTDESC>
                    <REPORTNAME>Vouchers</REPORTNAME>
                    <STATICVARIABLES>
                      <SVCURRENTCOMPANY>{companyName}</SVCURRENTCOMPANY>
                    </STATICVARIABLES>
                  </REQUESTDESC>
                  <REQUESTDATA>
                    <TALLYMESSAGE xmlns:UDF="TallyUDF">
                      <VOUCHER REMOTEID="{orderNumber}" VCHTYPE="{voucherType}" ACTION="Create" OBJVIEW="Invoice Voucher View">
                        <DATE>{orderDate}</DATE>
                        <EFFECTIVEDATE>{orderDate}</EFFECTIVEDATE>
                        <VOUCHERTYPENAME>{voucherType}</VOUCHERTYPENAME>
                        <VOUCHERNUMBER>{orderNumber}</VOUCHERNUMBER>
                        <REFERENCE>{orderNumber}</REFERENCE>
                        <PARTYLEDGERNAME>{supplierLedger}</PARTYLEDGERNAME>
                        <PARTYNAME>{supplierLedger}</PARTYNAME>
                        <BASICBUYERNAME>{companyName}</BASICBUYERNAME>
                        <PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>
                        <ISINVOICE>No</ISINVOICE>
                        {inventoryEntries}
                        <LEDGERENTRIES.LIST>
                          <LEDGERNAME>{supplierLedger}</LEDGERNAME>
                          <ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>
                          <ISPARTYLEDGER>Yes</ISPARTYLEDGER>
                          <AMOUNT>{totalAmount}</AMOUNT>
                        </LEDGERENTRIES.LIST>
                      </VOUCHER>
                    </TALLYMESSAGE>
                  </REQUESTDATA>
                </IMPORTDATA>
              </BODY>
            </ENVELOPE>
            """;

    /** One block per PO line; repeated and joined into {inventoryEntries}. */
    public static final String TALLY_PURCHASE_ORDER_ITEM = """
            <ALLINVENTORYENTRIES.LIST>
              <STOCKITEMNAME>{itemName}</STOCKITEMNAME>
              <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>
              <RATE>{rate}/{uom}</RATE>
              <AMOUNT>{amount}</AMOUNT>
              <ACTUALQTY> {qty} {uom}</ACTUALQTY>
              <BILLEDQTY> {qty} {uom}</BILLEDQTY>
              <BATCHALLOCATIONS.LIST>
                <GODOWNNAME>{godown}</GODOWNNAME>
                <BATCHNAME>{batchName}</BATCHNAME>
                <ORDERNO>{orderNumber}</ORDERNO>
                <AMOUNT>{amount}</AMOUNT>
                <ACTUALQTY> {qty} {uom}</ACTUALQTY>
                <BILLEDQTY> {qty} {uom}</BILLEDQTY>
                <ORDERDUEDATE JD="{dueDateJd}" P="{dueDate}">{dueDate}</ORDERDUEDATE>
              </BATCHALLOCATIONS.LIST>
              <ACCOUNTINGALLOCATIONS.LIST>
                <LEDGERNAME>{purchaseLedger}</LEDGERNAME>
                <ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>
                <AMOUNT>{amount}</AMOUNT>
              </ACCOUNTINGALLOCATIONS.LIST>
            </ALLINVENTORYENTRIES.LIST>
            """;



}
