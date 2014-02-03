<%@ page import="com.sampel.Student" %>



<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="student.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${studentInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: studentInstance, field: 'age', 'error')} required">
	<label for="age">
		<g:message code="student.age.label" default="Age" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="age" type="number" value="${studentInstance.age}" required=""/>
</div>

