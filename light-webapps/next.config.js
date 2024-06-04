/* eslint-disable @typescript-eslint/no-var-requires */
/** @type {import('next').NextConfig} */
const path = require('path');
const withLess = require('next-with-less');
const withTM = require('next-transpile-modules')([
    '@arco-design/web-react',
    '@arco-themes/react-ldp-theme',
]);

const CopyWebpackPlugin = require('copy-webpack-plugin'); // 添加这行
const setting = require("./src/settings.json");

module.exports = withLess(
    withTM({
        distDir: "dist",
        lessLoaderOptions: {
            lessOptions: {
                modifyVars: {
                    'arcoblue-6': setting.themeColor,
                },
            },
        },
        webpack: (config) => {
            config.module.rules.push({
                test: /\.svg$/,
                use: ['@svgr/webpack'],
            });

            config.plugins.push(
                new CopyWebpackPlugin({
                    patterns: [
                        { from: '**/*.{less,png,svg,ico,jpg,jpeg,gif}', to: '[path][name][ext]', context: 'src' },
                    ],
                })
            );

            return config;
        },
        async redirects() {
            return [
                {
                    source: '/',
                    destination: '/dashboard/workplace',
                    permanent: true,
                },
            ];
        },
        pageExtensions: ['tsx'],
    })
);