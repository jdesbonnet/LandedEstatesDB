<table width="100%" border="0" cellpadding="5" cellspacing="0" class="tableborder">
              <tr valign="top">
                <td width="50%"><h4>Faculty and Staff Services</h4>
                <ul class="bulletlist">
                    <li> <a title="Academic Web Service (Syllabus structure, Exam timetables)" href="https://www.mis.nuigalway.ie/regexam/mis_web_services_menu.asp" target="_blank"> Academic Web Service (Syllabus structure, Exam timetables)</a></li>
										<li> <a title="Student Records System &ndash; Access for Academics" href="http://www.nuigalway.ie/mis/acstaff.html"> Student Records System &ndash; Access for Academics</a> </li>
										<li> <a title="Postgraduate Applications Centre (PAC)" target="_blank" href="https://www.mis.nuigalway.ie/regexam/secure/pac_admin_link.asp">Postgraduate Applications Centre (PAC)</a> ** </li>										
                    <?php 
										// On Campus
										if (isOnCampus()) {
										// Admin Access Only - Vlan 2 or 20 Check
										if (isVlan(2) || isVlan(20)) {
										?>
										<li> <a title="Student Records System &ndash; Access for Admin" href="http://172.19.1.21/forms/frmservlet?config=QuercusPlus" target="_blank"> Student Records System &ndash; Access for Admin (Quercus)</a> *</li>
                    <li><a title="Payroll and HR System" href="http://ardroy9ias.mis.nuigalway.ie/servlet/f60servlet?config=LIVE" target="_blank">Payroll and HR System *</a></li>
                    <?php // End Vlan Check
										}
										?>
										<li> <a title="Reporting (Discoverer)" href="http://www.nuigalway.ie/mis/discoverer.html"> Reporting (Discoverer)</a> *</li>
										<?php // End On Campus
										}
										?> 
                  </ul>
                  <?php //On Campus
                  if (isOnCampus()) {
                  ?>
                  <h4>Financial Systems</h4>
                  <ul class="bulletlist">
                    <li><a title="Financial System - Agresso Purchasing Online" href="http://www.nuigalway.ie/mis/purchasing.html">Financial System - Agresso Purchasing Online</a> *</li>
                  </ul>                  
                  <?php
                  } // End On Campus
                  ?>
                  <h4>Procurements and Contract </h4>
                  <ul class="bulletlist">
                    <li><a title="Contract Feedback" href="http://www.nuigalway.ie/procure/internal/contract_feedback/">Contract Feedback</a> * </li>
                  </ul>                  
                  <h4>Library Services </h4>
                  <ul class="bulletlist">
                    <li> <a title="Library Catalogue" href="http://www.library.nuigalway.ie/"> Library</a></li>
                  </ul>
                  <h4>Professional Development, Human Resources</h4>
                  <ul class="bulletlist">
                  <li><a title="Staff Training &amp; Development (STDO)" href="http://www.nuigalway.ie/stafftraining/">Staff Training &amp; Development</a> </li>
                </ul>
                  <h4>Search</h4>
                  <ul class="bulletlist">
                    <li><a href="http://www.nuigalway.ie/search/">Search</a></li>
                  </ul>
                </td>
                <td width="50%"><h4>IT Services</h4>
                <ul class="bulletlist">
                    <li><a title="StaffMail (Exchange-based email)" href="https://staffmail.nuigalway.ie/">StaffMail (Exchange-based email)</a>   
                      <ul>
                        <li><a title="Webmail (Bodkin-based email)" href="http://webmail.nuigalway.ie/">Webmail (Bodkin-based email)</a></li>
                      </ul>
                  </li>
                    <li> <a title="Webmail (M.I.S. users)" href="http://www.mis.nuigalway.ie/exchange/"> Webmail (MIS users)</a> </li>
                    <li><a title="PC Suites" href="http://www.nuigalway.ie/cs/suites/suites.php">PC Suites</a></li>
                  </ul>
                  <?php // On Campus
									if (isOnCampus()) {
									?>
									<h4>Buildings Office</h4>
									<ul class="bulletlist">
                    <li><a href="http://10.2.151.117/workrequest/" title="Work Request System" target="_blank">Work Request System</a> * </li>
                  </ul>
									<?php } ?>
                  <h4>Teaching and Learning</h4>
                  <ul class="bulletlist">
                    <li><a title="Blackboard Learning System" href="http://blackboard.nuigalway.ie/">Blackboard Learning System</a> </li>
                  </ul>                  
                  <h4>Research</h4>
                  <ul class="bulletlist">
                    <li><a title="Research Support System" href="http://www.nuigalway.ie/research/vp_research/Rss/database.html">Research Support System</a></li>
                  </ul>                  
                  <h4>Intranets</h4>
                  <ul class="bulletlist">
                    <li> <a title="Arts Faculty" href="https://ishare.mis.nuigalway.ie:8001/NUIG/arts" target="_blank"> Arts Faculty</a> </li>
                    <li> <a title="Commerce Faculty" href="https://ishare.mis.nuigalway.ie:8001/commerce" target="_blank"> Commerce Faculty</a> </li>
                    <li> <a title="Engineering Faculty" href="https://ishare.mis.nuigalway.ie:8001/NUIG/eng" target="_blank"> Engineering Faculty</a> </li>
                    <li> <a title="Science Faculty" href="https://ishare.mis.nuigalway.ie:8001/NUIG/science/default.aspx" target="_blank"> Science Faculty</a></li>
                    <li><a href="https://ishare.mis.nuigalway.ie:8001/NUIG/aprc/default.aspx" target="_blank">Committees</a></li>
                  </ul>
                </td>
              </tr>
              <tr valign="top">
                <td colspan="2">* - <span class="bodysmall">On campus access only</span> <br>
                  ** - Only accessible if you are registered with the <a href="http://www.nuigalway.ie/postgrad/">Postgraduate Admission Office</a> as a PAC user </td>
              </tr>
          </table>