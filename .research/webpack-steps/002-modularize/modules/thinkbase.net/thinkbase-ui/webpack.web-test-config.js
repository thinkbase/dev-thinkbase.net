//Webpack configuration JUST FOR TEST
module.exports = {
    devtool: "source-map",  //Force create source map file
    entry: './web-test/test.js',
    output: {
        path: __dirname,
        filename: 'test-bundle.js'
    },
    module: {
        loaders: [
            { test: /\.css$/, loader: 'style-loader!css-loader' },
            { test: /\.(png|jpg)$/, loader: 'url-loader?limit=256' } // inline base64 URLs for <=256bytes images, direct URLs for the rest
        ]
    }

}
