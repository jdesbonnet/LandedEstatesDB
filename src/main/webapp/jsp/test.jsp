<%= request.getServerName() %>
<%
List<Family> families = em.createQuery("from Family").getResultList();
%>
Found <%=families.size()%> records.