<%--
  ~ Copyright 2000-2014 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="constants" class="org.bdd.reporting.teamcity.plugin.BDDReportingContstantsBean"/>

<l:settingsGroup title="BDD Configuration">

    <tr>
        <th>
            <label>Test Output Folder:</label>
        </th>
        <td>
            <span>
                <props:textProperty name="${constants.baseDir}" className="longField"/>
                <bs:vcsTree fieldId="${constants.baseDir}" treeId="${constants.baseDir}"/>
            </span>
            <span class="smallNote">The base directory from where to scan for report files</span>
            <span class="error" id="error_${constants.baseDir}"></span>
        </td>
    </tr>
    <tr>
        <th>
            <label>File search pattern:</label>
        </th>
        <td>
            <span>
                <props:textProperty name="${constants.pattern}" className="longField"/>
            </span>
            <span class="smallNote">The pattern to use for the search</span>
            <span class="error" id="error_${constants.pattern}"></span>
        </td>
    </tr>
    <tr>
        <th>
            <label>URL of the server:</label>
        </th>
        <td>
            <span>
                <props:textProperty name="${constants.url}" className="longField"/>
            </span>
            <span class="smallNote">The URL of the BDD Reporting server</span>
            <span class="error" id="error_${constants.url}"></span>
        </td>
    </tr>

    <tr>
        <th>
            <label>File(s) type:</label>
        </th>
        <td>
            <span>
                <props:selectProperty name="${constants.type}">
                    <props:option value="cucumber"><c:out value="Cucumber"/></props:option>
                    <props:option value="pickles"><c:out value="Pickles"/></props:option>
                    <%--<props:option value="specflow"><c:out value="SpecFlow"/></props:option>--%>
                </props:selectProperty>

            </span>
            <span class="smallNote">The type of report file being uploaded</span>
            <span class="error" id="error_${constants.type}"></span>
        </td>
    </tr>

</l:settingsGroup>