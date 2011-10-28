<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
	<meta name="decorator" content="logged" />
	<title><spring:message code="help.sub.title" /></title>
	<script type="text/javascript" src="<spring:url value="/js/logged/help.js" />"></script>
</head>

<body>
	<div class="prepend-2 span-20 append-2 last forms-subtitle">
		<h3 class="alt forms-border-separator">
			<spring:message code="help.sub.title" />
		</h3>
	</div>
	<div class="prepend-2 span-20 append-2 last">
		<div id="accordion" class="hide">
			<%-- What is the dashboard? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.dashboard.whatis" /></a>
				</h3>
				<div>
					<spring:message code="help.dashboard.whatis1" /><br />
					<spring:message code="help.dashboard.whatis2" /><br /><br />
					<spring:message code="help.dashboard.whatis3" />
				</div>
			</div>

			<%-- How to create a task? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.task.create" /></a>
				</h3>
				<div>
					<spring:message code="help.task.create1" /><br /><br />
					<ul>
						<li>
							<spring:message code="help.task.create2" /><br />
							<img src="<spring:url value="/images/logged/help/en_US/create_a_note1.png" />" /><br /><br /><br />
						</li>
						<li>
							<spring:message code="help.task.create3" /><br />
							<img src="<spring:url value="/images/logged/help/en_US/create_a_note2.png" />" /><br />
							<spring:message code="help.task.create4" />
						</li>
					</ul>
				</div>
			</div>

			<%-- How to create a category? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.category.create" /></a>
				</h3>
				<div>
					<spring:message code="help.category.create1" /><br />
					<img src="<spring:url value="/images/logged/help/en_US/create_a_category1.png" />" /><br /><br />
					<spring:message code="help.category.create2" /><br />
					<img src="<spring:url value="/images/logged/help/en_US/create_a_category2.png" />" /><br />
				</div>
			</div>

			<%-- What are categories useful for? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.category.useful" /></a>
				</h3>
				<div>
					<spring:message code="help.category.useful1" />
				</div>
			</div>

			<%-- What is the quick memo useful for? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.memo.useful" /></a>
				</h3>
				<div>
					<spring:message code="help.memo.useful1" /><br />
					<spring:message code="help.memo.useful2" />
				</div>
			</div>

			<%-- What are "Today" / "Tomorrow" / "This week"'s button useful for? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.datebt.useful" /></a>
				</h3>
				<div>
					<spring:message code="help.datebt.useful1" /><br />
					<spring:message code="help.datebt.useful2" /><br />
					<img src="<spring:url value="/images/logged/help/en_US/today.png" />" /><br /><br />
				</div>
			</div>

			<%-- I don't understand the statistics --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.stats.guide" /></a>
				</h3>
				<div>
					<spring:message code="help.stats.guide1" /><br />
					<img src="<spring:url value="/images/logged/help/en_US/stats.png" />" /><br />
					<spring:message code="help.stats.guide2" />
				</div>
			</div>

			<%-- Can I create a task directly assigned to a category? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.task.category" /></a>
				</h3>
				<div>
					<spring:message code="help.task.category1" /><br />
					<ul>
						<li>
							<spring:message code="help.task.category2" /><br />
							<spring:message code="help.task.category3" /><br />
							<img src="<spring:url value="/images/logged/help/en_US/select_category1.png" />" /><br />
							<spring:message code="help.task.category4" /><br /><br /><br />
						</li>
						<li>
							<spring:message code="help.task.category5" /><br />
							<img src="<spring:url value="/images/logged/help/en_US/select_category2.png" />" />
						</li>
					</ul>
				</div>
			</div>

			<%-- What happens when I delete a category which contains some notes? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.delete.category" /></a>
				</h3>
				<div>
					<spring:message code="help.delete.category1" /><br />
					<spring:message code="help.delete.category2" />
				</div>
			</div>

			<%-- Does the "delete account" feature really removes all my data? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.delete.account" /></a>
				</h3>
				<div>
					<spring:message code="help.delete.account1" /><br />
					<spring:message code="help.delete.account2" /><br />
					<spring:message code="help.delete.account3" /><br />
					<spring:message code="help.delete.account4" />
				</div>
			</div>

			<%-- I need a specific feature, can you develop it? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.feature.need" /></a>
				</h3>
				<div>
					<spring:message code="help.feature.need1" /><br />
					<spring:message code="help.feature.need2" />
				</div>
			</div>

			<%-- I found a bug, can you fix it? --%>
			<div>
				<h3>
					<a href="#"><spring:message code="help.found.bug" /></a>
				</h3>
				<div>
					<spring:message code="help.found.bug1" /><br />
					<spring:message code="help.found.bug2" />
				</div>
			</div>
		</div>
	</div>
</body>
</html>
