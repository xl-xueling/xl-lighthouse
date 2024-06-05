const path = require('path');
const replace = require('replace-in-file');
const glob = require('glob');

const files = glob.sync('dist/**/*.{d.ts,js,jsx,module.less}');

files.forEach(file => {
    const fileDir = path.dirname(file);
    const options = {
        files: file,
        from: /(@\/[^'"]+)/g,
        to: (match) => {
            const relativePath = path.relative(fileDir, path.join('dist', match.slice(2)));
            return `./${relativePath}`.replace(/\\/g, '/');
        },
    };

    try {
        const results = replace.sync(options);
        console.log("results is:" + JSON.stringify(results))
        console.log(`Modified files:`, results.join(', '));
    } catch (error) {
        console.error('Error occurred:', error);
    }
});