<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
	<meta name="decorator" content="front" />
</head>

<body>
	<br />
	<div id="welcome-div" class="main-box span-24 last">
		<div class="prepend-1 span-22 append-1 last">
			<h3 class="alt">
				<spring:message code="home.title1" /> <b><spring:message code="home.title2" /></b> <spring:message code="home.title3" />
			</h3>

			<%-- Slider --%>
		    <div id="wrapper">
		        <div class="slider-wrapper theme-default">
		            <div id="slider" class="nivoSlider">
						<img src="<spring:url value="/images/front/slider/en_US/slider1.png" />" alt="" title="<b><spring:message code="home.slider11" /></b><spring:message code="home.slider12" /> <b><spring:message code="home.slider13" /></b> <spring:message code="home.slider14" />" />
		                <img src="<spring:url value="/images/front/slider/en_US/slider2.png" />" alt="" title="<spring:message code="home.slider21" /> <b><spring:message code="home.slider22" /></b> <spring:message code="home.slider23" />" />
		                <img src="<spring:url value="/images/front/slider/en_US/slider3.png" />" alt="" title="<b><spring:message code="home.slider31" /></b> <spring:message code="home.slider32" />" />
		                <img src="<spring:url value="/images/front/slider/en_US/slider4.png" />" alt="" title="<spring:message code="home.slider41" /> <b><spring:message code="home.slider42" /></b> <spring:message code="home.slider43" />" />
		            </div>
		        </div>
		    </div>

			<%-- Sign up now button --%>
			<div class="signup-btn">
				<a class="signup-link" href="<spring:url value="/signup" />">
					<spring:message code="home.signup.now" />
				</a><br />
				<spring:message code="home.orlogin1" />
				<a href="<spring:url value="/login" />"><spring:message code="home.orlogin2" /></a>.
			</div>
		</div>

		<%-- Features --%>
		<div class="prepend-10 span-13 append-1 last">
			<ul class="features-list">
				<li><spring:message code="home.features.free" /></li>
				<li><spring:message code="home.features.easy" /></li>
				<li><spring:message code="home.features.unlimited" /></li>
				<li><spring:message code="home.features.noadvert" /></li>
				<li><spring:message code="home.features.respectful" /></li>
			</ul>
		</div>
	</div>
</body>
</html>
