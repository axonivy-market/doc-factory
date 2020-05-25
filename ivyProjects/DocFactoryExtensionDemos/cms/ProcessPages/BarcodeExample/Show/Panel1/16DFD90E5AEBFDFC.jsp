<%@ page import="ch.ivyteam.ivy.page.engine.jsp.IvyJSP"%><jsp:useBean id="ivy" class="ch.ivyteam.ivy.page.engine.jsp.IvyJSP" scope="session"/><!--ivyjsp -->
<table >
<tr ><td >CODE_128</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/code128.png", false)%>' alt="bildli"  /></td></tr>
<tr ><td >EAN_14</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/codeEAN_14.png", false)%>' alt="bildli" /></td></tr>
<tr ><td >ISBN</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/codeISBN.png", false)%>' alt="bildli" /></td></tr>
<tr ><td >QR</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/codeQR.png", false)%>' alt="bildli" /></td></tr>
<tr ><td>DOT_CODE</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/codeDOT.png", false)%>' alt="bildli" /></td></tr>
<tr ><td>DATA_MATRIX</td>
<td ><img src='<%=ivy.html.fileref("ivy_DocFactoryDemo/codeMatrix.png", false)%>' alt="bildli" /></td></tr>
</table>
<!--/ivyjsp -->