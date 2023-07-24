<html lang="UTF-8">
<head lang="en">
    <#import "../common/frame.ftl" as baseFrame>
    <title>Error - LightHouse DataPlatform</title>
    <@baseFrame.top />
</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper">
    <@baseFrame.header />
    <@baseFrame.leftMenu />
    <div class="content-wrapper" style="padding-top:30px;">
        <section class="content">
            <div class="error-page">
                <div class="" style="width:660px;">
                    <h3><i class="fa fa-warning text-yellow"></i>&nbsp;&nbsp;System Error</h3>
                    <p>
                        System Error,
                        you may <a href="/">return to HomePage</a> or try refresh again.
                    </p>
                </div>
            </div>
        </section>
    </div>
    <@baseFrame.rightMenu />
    <@baseFrame.footer />
</div>
</body>
<@baseFrame.tail />
</html>