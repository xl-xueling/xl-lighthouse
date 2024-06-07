const path = require('path');
const fs = require('fs');
const replace = require('replace-in-file');
const glob = require('glob');

const files = glob.sync('build/**/*.{d.ts,js,jsx,module.less}');

files.forEach(file => {
    const fileDir = path.dirname(file);
    const options = {
        files: file,
        from: /(@\/[^'"]+)/g,
        to: (match) => {
            const relativePath = path.relative(fileDir, path.join('build', match.slice(2)));
            return `./${relativePath}`.replace(/\\/g, '/');
        },
    };

    try {
        const results = replace.sync(options);
        console.log("Modified file relative path:" + JSON.stringify(results))
    } catch (error) {
        console.error('Error occurred:', error);
    }
});

const filePrefix = '_app';
const filesToDelete = glob.sync('build/pages/*');

console.log("Handling global.less conflicts start!");
filesToDelete.forEach(file => {
    if (path.basename(file).startsWith(filePrefix)) {
        fs.unlink(file, (err) => {
            if (err) {
                console.error(`Error deleting file ${file}:`, err);
            } else {
                console.log(`Deleted file: ${file}`);
            }
        });
    }
});
console.log("Handling global.less conflicts completed!")

