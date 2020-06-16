<!DOCTYPE html>
<%@ page import="ch.ivyteam.ivy.page.engine.jsp.IvyJSP"%> 
<%@ page import="ch.ivyteam.ivy.Advisor"%>
<%@ page import="ch.ivyteam.util.Version"%>
<jsp:useBean id="ivy" class="ch.ivyteam.ivy.page.engine.jsp.IvyJSP" scope="session"/>
<html>
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><%=Advisor.getAdvisor().getApplicationName()%> <%=Advisor.getAdvisor().getVersion().getVersionString(Version.DETAIL_PATCH,Version.FORM_SHORT)%></title>
  <link rel="shortcut icon" href="../../../shared/images/ivy_favicon_48.png" />

  <link rel="stylesheet" type="text/css" href="<%=ivy.style()%>"/>

  <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.js"></script>
  <![endif]-->
</head>

<body>

<div class="navbar navbar-top" role="navigation">
  <div class="container" id="headerdiv">
	  <a class="ivy-logo" href="<%=ivy.html.applicationHomeRef()%>">
       <%=ivy.cms.co("/Project/Banner")%>
      </a>
  </div>
</div>

<div class="container">
<!-- Content -->
<h3>
<%=ivy.content("Caption","String")%>&nbsp;
</h3>
<jsp:include page='<%=ivy.panel("Panel1")%>' flush="true"/>

<!-- Footer -->
<hr/>
<footer>
  <div id="layoutFooter">
    <%=ivy.cms.co("/Project/FooterText")%>
  </div>
</footer>
</div>
</body>
</html>