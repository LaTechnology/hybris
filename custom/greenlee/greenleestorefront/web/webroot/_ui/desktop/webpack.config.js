var webpack = require('webpack'),
    Clean = require('clean-webpack-plugin');


module.exports = {
    entry: {
        home: './resources/js/app',
    },

    output: {
        path: __dirname + "/theme-greenlee/",
        filename: 'js/[name].js'
    },

    watch: true,

    plugins: [

        new webpack.optimize.UglifyJsPlugin({
            minimize: true,
            output: {
                comments: false
            }
        }),
        //new webpack.optimize.DedupePlugin(),
        // Clean up build directory before every build

        //new Clean(['./theme-greenlee/js', './build'])
    ],

    devServer: {
        contentBase: './theme-greenlee/js'
    }

};
