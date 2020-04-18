<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--  ----------------------------------------------------------------------  -->
<!--  NOTE: Please add the following <META> element to your page <HEAD>.      -->
<!--  If necessary, please modify the charset parameter to specify the        -->
<!--  character set of your HTML page.                                        -->
<!--  ----------------------------------------------------------------------  -->
<div class="contactus-form saleforceform">
    <META HTTP-EQUIV="Content-type" CONTENT="text/html; charset=UTF-8">

        <!--  ----------------------------------------------------------------------  -->
        <!--  NOTE: Please add the following <FORM> element to your page.             -->
        <!--  ----------------------------------------------------------------------  -->
        <form action="${salesforce_form_action}"
					method="POST">
					<input type="hidden" name="orgid" value="${salesforce_form_orgid}">
					<input type=hidden name="retURL" value="${salesforce_form_returl}">
					<input type=hidden name="recordType" id="recordType" value="${salesforce_form_recordtype}">

                    <!--  ----------------------------------------------------------------------  -->
                    <!--  NOTE: These fields are optional debugging elements. Please uncomment    -->
                    <!--  these lines if you wish to test in debug mode.                          -->
                    <!--  <input type="hidden" name="debug" value=1>                              -->
                    <!--  <input type="hidden" name="debugEmail"                                  -->
                    <!--  value="jhoang@greenlee.textron.com">                                    -->
                    <!--  ----------------------------------------------------------------------  -->
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="First_Name__c">First Name</label>
                            <input id="First_Name__c" maxlength="25" name="First_Name__c" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="Last_Name__c">Last Name</label>
                            <input id="Last_Name__c" maxlength="35" name="Last_Name__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Company_Name__c">Company</label>
                            <input id="Company_Name__c" maxlength="30" name="Company_Name__c" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="Title_Position__c">Title / Position</label>
                            <input id="Title_Position__c" maxlength="25" name="Title_Position__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Business_Type__c">Business Type</label>
                            <input id="Business_Type__c" maxlength="30" name="Business_Type__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Address_1__c">Address 1</label>
                            <input id="Address_1__c" maxlength="50" name="Address_1__c" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="Address_2__c">Address 2</label>
                            <input id="Address_2__c" maxlength="50" name="Address_2__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="City__c">City</label>
                            <input id="City__c" maxlength="35" name="City__c" size="20" type="text" class="form-control"/>
                        </div>

                        <div class="form-group col-md-6">
                            <label for="State_c__c">State / Province / Region</label>
                            <input id="State_c__c" maxlength="20" name="State_c__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Postal_Code__c">Postal Code</label>
                            <input id="Postal_Code__c" maxlength="6" name="Postal_Code__c" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="Country__c">Country</label>
                            <div class="controls isBorder">
                                <select id="Country__c" name="Country__c" title="Country" class="custom-select">
                                    <option value="">--None--</option><option value="US - USA">US - USA</option>
<option value="AF - Afghanistan">AF - Afghanistan</option>
<option value="AL - Albania">AL - Albania</option>
<option value="DZ - Algeria">DZ - Algeria</option>
<option value="VI - Amer. Virgin Is.">VI - Amer. Virgin Is.</option>
<option value="AD - Andorra">AD - Andorra</option>
<option value="AO - Angola">AO - Angola</option>
<option value="AI - Anguilla">AI - Anguilla</option>
<option value="AQ - Antarctica">AQ - Antarctica</option>
<option value="AG - Antigua / Barbuda">AG - Antigua / Barbuda</option>
<option value="AR - Argentina">AR - Argentina</option>
<option value="AM - Armenia">AM - Armenia</option>
<option value="AW - Aruba">AW - Aruba</option>
<option value="AU - Australia">AU - Australia</option>
<option value="AT - Austria">AT - Austria</option>
<option value="AZ - Azerbaijan">AZ - Azerbaijan</option>
<option value="BS - Bahamas">BS - Bahamas</option>
<option value="BH - Bahrain">BH - Bahrain</option>
<option value="BD - Bangladesh">BD - Bangladesh</option>
<option value="BB - Barbados">BB - Barbados</option>
<option value="BY - Belarus">BY - Belarus</option>
<option value="BE - Belgium">BE - Belgium</option>
<option value="BZ - Belize">BZ - Belize</option>
<option value="BJ - Benin">BJ - Benin</option>
<option value="BM - Bermuda">BM - Bermuda</option>
<option value="BT - Bhutan">BT - Bhutan</option>
<option value="BO- Bolivia">BO- Bolivia</option>
<option value="BA - Bosnia - Herz.">BA - Bosnia - Herz.</option>
<option value="BW - Botswana">BW - Botswana</option>
<option value="BV - Bouvet Island">BV - Bouvet Island</option>
<option value="BR - Brazil">BR - Brazil</option>
<option value="IO - Brit.Ind.Oc.Ter">IO - Brit.Ind.Oc.Ter</option>
<option value="VG - Brit.Virgin Is.">VG - Brit.Virgin Is.</option>
<option value="BN - Brunei">BN - Brunei</option>
<option value="BG- Bulgaria">BG- Bulgaria</option>
<option value="BF- Burkina - Fasco">BF- Burkina - Fasco</option>
<option value="BI - Burundi">BI - Burundi</option>
<option value="KH - Cambodia">KH - Cambodia</option>
<option value="CM - Cameroon">CM - Cameroon</option>
<option value="CA - Canada">CA - Canada</option>
<option value="CV - Cape Verde">CV - Cape Verde</option>
<option value="KY - Cayman Islands">KY - Cayman Islands</option>
<option value="CF - Central Afr. Rep">CF - Central Afr. Rep</option>
<option value="TD - Chad">TD - Chad</option>
<option value="CL - Chile">CL - Chile</option>
<option value="CN - China">CN - China</option>
<option value="CX - Christmas Island">CX - Christmas Island</option>
<option value="CC - Coconut Islands">CC - Coconut Islands</option>
<option value="CO - Colombia">CO - Colombia</option>
<option value="KM - Comoros">KM - Comoros</option>
<option value="CD - Congo">CD - Congo</option>
<option value="CG - Congo">CG - Congo</option>
<option value="CK - Cook Islands">CK - Cook Islands</option>
<option value="CR - Costa Rica">CR - Costa Rica</option>
<option value="HR - Croatia">HR - Croatia</option>
<option value="CY - Cyprus">CY - Cyprus</option>
<option value="CZ - Czech Republic">CZ - Czech Republic</option>
<option value="DK - Denmark">DK - Denmark</option>
<option value="DJ - Djibouti">DJ - Djibouti</option>
<option value="DM - Dominica">DM - Dominica</option>
<option value="DO - Dominican Rep.">DO - Dominican Rep.</option>
<option value="AN - Dutch Antilles">AN - Dutch Antilles</option>
<option value="TP East Timor">TP East Timor</option>
<option value="EC - Ecuador">EC - Ecuador</option>
<option value="EG - Egypt">EG - Egypt</option>
<option value="SV - El Salvador">SV - El Salvador</option>
<option value="GQ - Equatorial Gui.">GQ - Equatorial Gui.</option>
<option value="ER - Eritrea">ER - Eritrea</option>
<option value="EE - Estonia">EE - Estonia</option>
<option value="ET - Ethiopia">ET - Ethiopia</option>
<option value="FK - Falkland Islands">FK - Falkland Islands</option>
<option value="FO - Faroe Islands">FO - Faroe Islands</option>
<option value="FJ - Fiji">FJ - Fiji</option>
<option value="FI - Finland">FI - Finland</option>
<option value="FR - France">FR - France</option>
<option value="PF - Frenc.Polynsia">PF - Frenc.Polynsia</option>
<option value="GF - French Guayana">GF - French Guayana</option>
<option value="TF - French S Territ">TF - French S Territ</option>
<option value="GA - Gabon">GA - Gabon</option>
<option value="GM - Gambia">GM - Gambia</option>
<option value="GE - Georgia">GE - Georgia</option>
<option value="DE - Germany">DE - Germany</option>
<option value="GH - Ghana">GH - Ghana</option>
<option value="GI - Gibraltar">GI - Gibraltar</option>
<option value="GR - Greece">GR - Greece</option>
<option value="GL - Greenland">GL - Greenland</option>
<option value="GD - Grenada">GD - Grenada</option>
<option value="GP - Guadeloupe">GP - Guadeloupe</option>
<option value="GU - Guam">GU - Guam</option>
<option value="GT - Guatemala">GT - Guatemala</option>
<option value="GG - Guernsey">GG - Guernsey</option>
<option value="GN - Guinea">GN - Guinea</option>
<option value="GW - Guinea - Bissau">GW - Guinea - Bissau</option>
<option value="GY - Guyana">GY - Guyana</option>
<option value="HT - Haiti">HT - Haiti</option>
<option value="HM - Heard / McDon.Isl">HM - Heard / McDon.Isl</option>
<option value="HN - Honduras">HN - Honduras</option>
<option value="HK - Hong Kong">HK - Hong Kong</option>
<option value="HU - Hungary">HU - Hungary</option>
<option value="IS - Iceland">IS - Iceland</option>
<option value="IN - India">IN - India</option>
<option value="ID - Indonesia">ID - Indonesia</option>
<option value="IQ - Iraq">IQ - Iraq</option>
<option value="IE - Ireland">IE - Ireland</option>
<option value="IM - Isle of Man">IM - Isle of Man</option>
<option value="IL - Israel">IL - Israel</option>
<option value="IT - Italy">IT - Italy</option>
<option value="CI - Ivory Coast">CI - Ivory Coast</option>
<option value="JM - Jamaica">JM - Jamaica</option>
<option value="JP - Japan">JP - Japan</option>
<option value="JE - Jersey">JE - Jersey</option>
<option value="JO - Jordan">JO - Jordan</option>
<option value="KZ - Kazakhstan">KZ - Kazakhstan</option>
<option value="KE - Kenya">KE - Kenya</option>
<option value="KI - Kiribati">KI - Kiribati</option>
<option value="KW - Kuwait">KW - Kuwait</option>
<option value="KG - Kyrgyzstan">KG - Kyrgyzstan</option>
<option value="LA - Laos">LA - Laos</option>
<option value="LV - Lativa">LV - Lativa</option>
<option value="LB - Lebanon">LB - Lebanon</option>
<option value="LS - Lesotho">LS - Lesotho</option>
<option value="LR - Liberia">LR - Liberia</option>
<option value="LY - Libya">LY - Libya</option>
<option value="LI - Liechtenstein">LI - Liechtenstein</option>
<option value="LT - Lithuania">LT - Lithuania</option>
<option value="LU - Luxembourg">LU - Luxembourg</option>
<option value="MO - Macau">MO - Macau</option>
<option value="MK - Macedonia">MK - Macedonia</option>
<option value="MG - Madagascar">MG - Madagascar</option>
<option value="MW - Malawi">MW - Malawi</option>
<option value="MY - Malaysia">MY - Malaysia</option>
<option value="MV - Maidives">MV - Maidives</option>
<option value="ML - Mali">ML - Mali</option>
<option value="MT - Malta">MT - Malta</option>
<option value="MH - Marshall Islnds">MH - Marshall Islnds</option>
<option value="MQ - Martinique">MQ - Martinique</option>
<option value="MR - Murentania">MR - Murentania</option>
<option value="MU - Mauritius">MU - Mauritius</option>
<option value="YT - Mayotte">YT - Mayotte</option>
<option value="MX - Mexico">MX - Mexico</option>
<option value="FM - Micronesia">FM - Micronesia</option>
<option value="UM - Minor Outl.Isl">UM - Minor Outl.Isl</option>
<option value="MD - Moldavia">MD - Moldavia</option>
<option value="MC - Monaco">MC - Monaco</option>
<option value="MN - Mongolia">MN - Mongolia</option>
<option value="ME - Montenegro">ME - Montenegro</option>
<option value="MS - Montserrat">MS - Montserrat</option>
<option value="MA - Morocco">MA - Morocco</option>
<option value="MZ - Mozambique">MZ - Mozambique</option>
<option value="MM - Mayanmar">MM - Mayanmar</option>
<option value="MP - N.Mariana Islnd">MP - N.Mariana Islnd</option>
<option value="NA - Namibia">NA - Namibia</option>
<option value="NR - Nauru">NR - Nauru</option>
<option value="NP - Nepal">NP - Nepal</option>
<option value="NL - Netherlands">NL - Netherlands</option>
<option value="NC - New Caledonia">NC - New Caledonia</option>
<option value="NZ - New Zealand">NZ - New Zealand</option>
<option value="NI - Nicaragua">NI - Nicaragua</option>
<option value="NE - Niger">NE - Niger</option>
<option value="NG - Nigeria">NG - Nigeria</option>
<option value="NU - Niue Islands">NU - Niue Islands</option>
<option value="NF - Norfolk Island">NF - Norfolk Island</option>
<option value="NO - Norway">NO - Norway</option>
<option value="OM - Oman">OM - Oman</option>
<option value="PK - Pakistan">PK - Pakistan</option>
<option value="PW - Palau">PW - Palau</option>
<option value="PA - Panama">PA - Panama</option>
<option value="PG - Pap.New Guinea">PG - Pap.New Guinea</option>
<option value="PY - Paraguay">PY - Paraguay</option>
<option value="PE - Peru">PE - Peru</option>
<option value="PH - Philippines">PH - Philippines</option>
<option value="PN - Pitcairn Islnds">PN - Pitcairn Islnds</option>
<option value="PL - Poland">PL - Poland</option>
<option value="PT - Portugal">PT - Portugal</option>
<option value="PR - Puerto Rico">PR - Puerto Rico</option>
<option value="QA - Qatar">QA - Qatar</option>
<option value="RE - Reunion">RE - Reunion</option>
<option value="RO - Romania">RO - Romania</option>
<option value="RU - Russian Fed.">RU - Russian Fed.</option>
<option value="RW - Rwanda">RW - Rwanda</option>
<option value="ST - S.Tome, Principe">ST - S.Tome, Principe</option>
<option value="AS - Samoa, America">AS - Samoa, America</option>
<option value="SM - San Marino">SM - San Marino</option>
<option value="SA - Saudi Arabia">SA - Saudi Arabia</option>
<option value="SN - Senegal">SN - Senegal</option>
<option value="RS - Serbia">RS - Serbia</option>
<option value="SC - Seyechelles">SC - Seyechelles</option>
<option value="SL - Sierra Leone">SL - Sierra Leone</option>
<option value="SG - Singapore">SG - Singapore</option>
<option value="SK - Slovakia">SK - Slovakia</option>
<option value="SI - Slovenia">SI - Slovenia</option>
<option value="SB - Solomon Islands">SB - Solomon Islands</option>
<option value="SO - Somalia">SO - Somalia</option>
<option value="ZA - South Africa">ZA - South Africa</option>
<option value="ES - Spain">ES - Spain</option>
<option value="LK - Sri - Lanka">LK - Sri - Lanka</option>
<option value="KN - St Kitts&amp;Nev">KN - St Kitts&amp;Nev</option>
<option value="SH - St. Helena">SH - St. Helena</option>
<option value="LC - St. Lucia">LC - St. Lucia</option>
<option value="VC - St. Vincent">VC - St. Vincent</option>
<option value="PM - St. Pier, Miquel">PM - St. Pier, Miquel</option>
<option value="GS - Sth Sandwitch Is">GS - Sth Sandwitch Is</option>
<option value="SR -  Suriname">SR -  Suriname</option>
<option value="SJ - Svalbard">SJ - Svalbard</option>
<option value="SZ - Swaziland">SZ - Swaziland</option>
<option value="SE - Sweden">SE - Sweden</option>
<option value="CH - Switzerland">CH - Switzerland</option>
<option value="TW - Taiwan">TW - Taiwan</option>
<option value="TJ - Tajikstan">TJ - Tajikstan</option>
<option value="TZ - Tanzania">TZ - Tanzania</option>
<option value="Th - Thailand">Th - Thailand</option>
<option value="TG - Togo">TG - Togo</option>
<option value="TK - Tokelau Islands">TK - Tokelau Islands</option>
<option value="TO - Tonga">TO - Tonga</option>
<option value="TT - Trinidad, Tobago">TT - Trinidad, Tobago</option>
<option value="TN - Tunisia">TN - Tunisia</option>
<option value="TR - Turkey">TR - Turkey</option>
<option value="TM - Turkmenistan">TM - Turkmenistan</option>
<option value="TC - Turksh Caicosin">TC - Turksh Caicosin</option>
<option value="TV - Tuvalu">TV - Tuvalu</option>
<option value="AE - UAE">AE - UAE</option>
<option value="UG - Uganda">UG - Uganda</option>
<option value="UA - Ukraine">UA - Ukraine</option>
<option value="GB - United Kingdom">GB - United Kingdom</option>
<option value="UY - Uruguay">UY - Uruguay</option>
<option value="UZ - Uzbekistan">UZ - Uzbekistan</option>
<option value="VU - Vanuatu">VU - Vanuatu</option>
<option value="VA - Vatican City">VA - Vatican City</option>
<option value="VE - Venezuela">VE - Venezuela</option>
<option value="VN - Vietnam">VN - Vietnam</option>
<option value="WF - Wallis, Futuna">WF - Wallis, Futuna</option>
<option value="EH - West Sahara">EH - West Sahara</option>
<option value="WS - Western Samoa">WS - Western Samoa</option>
<option value="YE - Yemen">YE - Yemen</option>
<option value="YU - Yugoslavia">YU - Yugoslavia</option>
<option value="ZM - Zambia">ZM - Zambia</option>
<option value="ZW - Zimbabwe">ZW - Zimbabwe</option>


                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="email">Email</label>
                            <input id="email" maxlength="80" name="email" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="phone">Phone</label>
                            <input id="phone" maxlength="40" name="phone" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Type_of_Request__c">Type of Request</label>
                            <div class="controls isBorder">
                                 <select  id="Type_of_Request__c" name="Type_of_Request__c" title="Type of Request" class="custom-select"><option value="">--None--</option><option value="Product Feedback">Product Feedback</option>
<option value="Product Support">Product Support</option>
<option value="Website Suggestion / Question">Website Suggestion / Question</option>
<option value="Pricing">Pricing</option>
<option value="Other">Other</option>
<option value="Request a Demo on a Tool">Request a Demo on a Tool</option>
<option value="Request a Rental on a Tool">Request a Rental on a Tool</option>
<option value="Request Product Information">Request Product Information</option>
</select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-6">
                            <label for="Enter_5M_Number__c">Greenlee Internal Part Number</label>
                            <input id="Enter_5M_Number__c" maxlength="25" name="Enter_5M_Number__c" size="20" type="text" class="form-control"/>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="Enter_Catalog_Number__c">Enter Catalog Number</label>
                            <input id="Enter_Catalog_Number__c" maxlength="25" name="Enter_Catalog_Number__c" size="20" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-12">
                            <label for="description">Description</label>
                            <textarea name="description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
							<div class="checkbox checkbox-group">
							 
						   		<input  id="Privacy_Policy_Consent__c" name="Privacy_Policy_Consent__c" type="checkbox" value="1" />
						   			<c:url var="privacyPolicyURL" value="/policies-and-terms"></c:url>
						   		<label for="Privacy_Policy_Consent__c">I acknowledge that I have read and expressly consent to the collection and usage of this information as described in the <a href="${privacyPolicyURL}">Privacy Policy.</a>
						   		</label>
						   	</div>
						</div>
						<div class="form-group">
							<div class="checkbox checkbox-group">
							
						   		<input  id="Sales_and_Marketing_Info_request__c" name="Sales_and_Marketing_Info_request__c" type="checkbox" value="1" />
						   			 <label for="Sales_and_Marketing_Info_request__c">I consent to receiving additional sales, marketing and other information about the products and services of Greenlee and its affiliates. You may unsubscribe at any time.
						   		</label>
						   	</div> 	
						   	</div>
                    <div class="form-group">
                        <input class="btn-white btn-popup" type="submit" name="submit"/>
                    </div>

                </form>
            </div>
