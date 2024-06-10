/* eslint-disable @typescript-eslint/no-var-requires */
/** @type {import('next').NextConfig} */
const path = require('path');
const withLess = require('next-with-less');
const withTM = require('next-transpile-modules')([
    '@arco-design/web-react',
    '@arco-themes/react-ldp-theme',
]);

const CopyWebpackPlugin = require('copy-webpack-plugin');
const ThreadLoader = require('thread-loader');
const threadLoaderOptions = {
    workers: 2,
};
ThreadLoader.warmup(threadLoaderOptions, ['babel-loader']);

const setting = require("./src/settings.json");

module.exports = withLess(
    withTM({
        distDir: "build",
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
            },
                {
                    test: /\.js$/,
                    include: path.resolve('src'),
                    use: [
                        'cache-loader',
                        {
                            loader: 'thread-loader',
                            options: threadLoaderOptions,
                        },
                        'babel-loader',
                    ],
                }
            );

            config.plugins.push(
                new CopyWebpackPlugin({
                    patterns: [
                        { from: '**/*.{less,png,svg,ico,jpg,jpeg,gif}', to: '[path][name][ext]', context: 'src' },
                    ],
                })
            );
            config.watchOptions = {
                poll: 1000,
                ignored: /node_modules/,
            };


            config.parallelism = 4;

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