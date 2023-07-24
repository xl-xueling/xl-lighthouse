<html lang="UTF-8">
<head lang="en">
    <#import "../common/frame.ftl" as baseFrame>
    <title>404 - LightHouse DataPlatform</title>
    <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
    <@baseFrame.header />
    <@baseFrame.leftMenu />
    <div class="content-wrapper" style="padding-top:30px;">
        <section class="content">
            <div class="error-page">
                <div class="">
                    <h3><i class="fa fa-warning text-yellow"></i>&nbsp;&nbsp;404</h3>
                    <p>
                        We could not find the page you were looking for!
                    </p>
                </div>
            </div>
        </section>
    </div>
    <@baseFrame.rightMenu />
    <@baseFrame.footer />
</div>
<@baseFrame.tail />
</body>
</html>