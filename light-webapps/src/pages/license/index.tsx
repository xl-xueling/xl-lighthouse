import React from "react";
import Markdown from "markdown-to-jsx";
import licenseContent from "@/pages/license/license.md?raw";

function License() {

    return (
        <div style={{paddingLeft:'20px',paddingTop:'20px',color:'var(--color-text-1)'}}>
            <Markdown>
                {licenseContent}
            </Markdown>;
        </div>
    )
}

export default License;