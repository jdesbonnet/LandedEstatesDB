<%@include file="_header.jsp" %>
<%= request.getServerName() %>
<%
List<Family> families = em.createQuery("from Family").getResultList();
%>
Found <%=families.size()%> records.