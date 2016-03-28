//Webpack configuration for distribution
module.exports = {
    devtool: "source-map",      //Force create source map file
    entry: './src/index.js',
    output: {
        path: __dirname + "/dist",
        publicPath: "/dist/",    //Importment: MUST refer `bundle.js` with `<script src="/dist/bundle.js"></script>`
        filename: 'bundle.js'
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
            { test: /\.html$/, loader: 'html-loader' }
        ]
    }
}
