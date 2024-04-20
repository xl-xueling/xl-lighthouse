/* eslint-disable @typescript-eslint/no-var-requires */
const path = require('path');
const {
  override,
  addWebpackModuleRule,
  addWebpackPlugin,
  addWebpackAlias,
} = require('customize-cra');
const ArcoWebpackPlugin = require('@arco-plugins/webpack-react');
const addLessLoader = require('customize-cra-less-loader');
const setting = require('./src/settings.json');

module.exports = {
  webpack: override(
      (config) => {
          config.resolve.extensions = ['.ts', '.tsx', '.mjs', '.js', '.jsx', 'json', '.gql', '.graphql']
          config.module.rules.unshift({
              test: /\.m?jsx?$/,
              resolve: {
                  fullySpecified: false,
              },
          });
          return config;
      },
    addLessLoader({
      lessLoaderOptions: {
        lessOptions: {},
      },
    }),
    addWebpackModuleRule({
      test: /\.svg$/,
      loader: '@svgr/webpack',
    }),
    addWebpackPlugin(
      new ArcoWebpackPlugin({
        // theme: '@arco-themes/react-arco-pro',
          theme: '@arco-themes/react-ldp-theme',
        modifyVars: {
          'arcoblue-6': setting.themeColor,
        },
      })
    ),
    addWebpackAlias({
      '@': path.resolve(__dirname, 'src'),
    })
  ),
};
