//Webpack configuration JUST FOR TEST
module.exports = {
    devtool: "source-map",  //Force create source map file
    entry: './web-test/test.js',
    output: {
        path: __dirname,
        filename: 'test-bundle.js'
    },
    resolve: {
        root: [process.cwd(), process.cwd() + '/src', process.cwd() + '/node_modules'],
        alias: {},
        extensions: ['', '.js', '.css', '.html', '.png', '.jpg']
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.(png|jpg)$/, loader: 'url-loader?limit=256' }, // inline base64 URLs for <=256bytes images, direct URLs for the rest
            { test: /\.html$/, loader: 'html-loader' },

            // **IMPORTANT** This is needed so that each bootstrap js file required by
            // bootstrap-webpack has access to the jQuery object
            { test: /bootstrap\/js\//, loader: 'imports?jQuery=jquery' },

            // Needed for the css-loader when [bootstrap-webpack](https://github.com/bline/bootstrap-webpack)
            // loads bootstrap's css.
            { test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,   loader: "url?limit=10000&minetype=application/font-woff" },
            { test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,  loader: "url?limit=10000&minetype=application/font-woff" },
            { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,    loader: "url?limit=10000&minetype=application/octet-stream" },
            { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,    loader: "file" },
            { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,    loader: "url?limit=10000&minetype=image/svg+xml" }
        ]
    }
}
