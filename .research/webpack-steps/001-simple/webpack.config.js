module.exports = {
    devtool: "source-map",  //Force create source map file
    entry: './src/main.js',
    output: {
        path: __dirname,
        filename: 'bundle.[name].js'
    }
}
