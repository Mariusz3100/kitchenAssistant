
<header id="header" class="header header-hide">
	<div class="container">

		<div id="logo" class="pull-left">
			<h1>
				<a href="#body" class="scrollto"><span>e</span>Startup</a>
			</h1>
			<!-- Uncomment below if you prefer to use an image logo -->
			<!-- <a href="#body"><img src="img/logo.png" alt="" title="" /></a>-->
		</div>

		<nav id="nav-menu-container">
			<ul class="nav-menu">
				<li class="menu-active"><a href="/bindex">Home</a></li>
				<li class="menu-has-children"><a>Parse Recipe</a>
					<ul>
						<li><a href="/b_engRecipeUrlForm">By URL</a></li>
						<li><a href="/b_engRecipeForm">By phrase</a></li>
					</ul>
				</li>
	 			<li class="menu-has-children"><a>Search for Produkts</a>
	 				<ul>
						<li><a href="${productByUrlSuffix}">By URL</a></li>
						<li><a href="${productByNameSuffix }">By phrase</a></li>
					</ul>
	 			
	 			</li>
				<li class="menu-has-children"><a>Agents</a>
					<ul>
						<li><a href="${startAgentSystemSuffix}">Start Agent System</a></li>
						<li><a href="/b_agentList">List of all agents in the system</a></li>
					</ul></li>
				<li>
				<li class="menu-has-children"><a>Get Nutrients</a>
					<ul>
						<li><a href="${nutrientByNameSuffix}">For a product name</a></li>
						<li><a href="${nutrientByNdbnoSuffix}">For usda ndbno</a></li>
					</ul>
				</li>
				<li class="menu-has-children"><a>Google data</a>
					<ul>
						<li><a href="${googleAuthorisationSuffix}">Authorise access to google drive</a></li>
						<li><a href="${googleDeleteSuffix}">Remove access to google drive</a></li>
						<li><a href="${googleGetDataSuffix}">Get Diet and Health restrictions</a></li>
						<li><a href="/b_getAllLabels">List all available labels</a></li>
						<li><a href="/b_editLabels">Edit Labels in Google Drive</a></li>
						
					</ul></li>
			</ul>
		</nav>
		<!-- #nav-menu-container -->
	</div>
</header>
<!-- #header -->