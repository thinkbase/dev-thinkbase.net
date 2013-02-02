/**
 * required:
 *  1) x3dom.js - http://www.x3dom.org/
 *  2) json2.js - https://github.com/douglascrockford/JSON-js
 */
(function(){
    var _CONSTANTS = {
        prefixContainter: "CONTAINER",
        prefixBox       : "BOX",
        //x3dom elements tag name
        tagX3D         : "X3d",
        tagScene       : "Scene",
        tagViewport    : "Viewport",
        tagBackground  : "Background",
        tagTransform   : "Transform",
        tagShape       : "Shape",
        tagAppearance  : "Appearance",
        tagMaterial    : "Material",
        tagBox         : "Box",
        //Transparency for highlight state
        HighlightTransparency: "0"
    };
    var _GLOBAL_CFG = {
        BOX_COLOR_POOL: [       //The Colors for build the box
            //The W3C 16 colors
            /* white */ "yellow", "fuchsia", "red", "silver", "gray", "olive", "purple",
            "maroon", "aqua", "lime", "teal", "green", "blue", "navy" /* black */
        ],
        CONTAINER_ROTATIONS: {  //The rotations for viewport of container
            "default": "1.0 -0.5 0 0.4",
            "front": "0 0 1 0",
            "top": "1 0 0 1.57",
            "right": "0 -0.5 0 1.57"
        }
    };
    var _DEFAULT_CONTAINER_CFG = {
        size: {                 //The measurements of container, in meters
            width: 11.8, height: 2.18, depth: 2.13,     //40 inch DRYCONTAINER
            sideSize: 0.001         //Size of every side, in meters
        },
        materialAttributes:{    //The meatrial attributes of container's back, bottom, and left side;
            back:   {diffuseColor: "0.3 0 0", transparency: 0.5},
            bottom: {diffuseColor: "0 0.3 0", transparency: 0.5},
            left:   {diffuseColor: "0 0 0.3", transparency: 0.5},
        },
        auxLine: {              //Auxiliary line settings
            size: 0.01,             //Size of the auxiliary line to show in container, in meters
            materialAttributes: {   //Meatrial attributes of the auxiliary line
                diffuseColor: "gray", transparency: 0.5
            }
        }
    };
    var _DEFAULT_BOX_CFG = {
        position: {      //Position of box, in meters
            x:0, y:0, z:0
        },
        size: {         //Size of box, in meters
            width: 1.0, height: 1.0, depth: 1.0 
        },
        materialAttributes: {   //Meatrial attributes of box
            diffuseColor: "white", transparency: 0.5
        }
    };
    var _ID_INC_REGISTER = 0;       //Remember the auto-increasing ID counter
    var _COLOR_REGISTER = 0;        //Remember the color index
    var _BOXID_FILL3D_MAPPING = {}; //Remember boxId and it's Fill3D object
    var _UTILS = {  //Utility functions
        clone: function(obj){   //Clone an object, for duplicate the default configs
            var jsonStr = JSON.stringify(obj);
            return JSON.parse(jsonStr);
        },
        nextId: function(){
            return _ID_INC_REGISTER ++;
        },
        nextColor: function(){
            if (_COLOR_REGISTER >= _GLOBAL_CFG.BOX_COLOR_POOL.length){
                _COLOR_REGISTER = 0;
            }
            var color = _GLOBAL_CFG.BOX_COLOR_POOL[_COLOR_REGISTER];
            _COLOR_REGISTER++;
            return color;
        },
        addBox: function (fill3d, boxCfg, isBox, boxId){
            if (null==boxId) boxId = _UTILS.nextId();
        
            if (isBox){
                _BOXID_FILL3D_MAPPING[boxId] = fill3d;
                fill3d.allBoxIds[fill3d.allBoxIds.length] = boxId;
            }
        
            var rootTransform = fill3d.rootTransform;
            var bc = boxCfg;
            var cc = fill3d.containerCfg;
        
            // x / y / z 的偏移量需要根据 container 的 width / height / depth 重新定位
            var offsetX = (null==cc.size.width )?0:(bc.position.x - (cc.size.width - bc.size.width)/2);
            var offsetY = (null==cc.size.height)?0:(bc.position.y - (cc.size.height-bc.size.height)/2);
            var offsetZ = (null==cc.size.depth )?0:(bc.position.z - (cc.size.depth - bc.size.depth)/2);

            var t = document.createElement(_CONSTANTS.tagTransform);
            t.setAttribute("translation", offsetX + " " + offsetY + " " + offsetZ);
            
            var s = document.createElement(_CONSTANTS.tagShape);
            t.appendChild(s);

            var a = document.createElement(_CONSTANTS.tagAppearance);
            s.appendChild(a);
            var m = document.createElement(_CONSTANTS.tagMaterial);
            for (var attr in boxCfg.materialAttributes){
                var attrVal = boxCfg.materialAttributes[attr];
                m.setAttribute(attr, attrVal);
            }
            a.appendChild(m);
            
            var b = document.createElement(_CONSTANTS.tagBox);
            b.setAttribute("id", boxId);
            b.setAttribute("size", bc.size.width + " " + bc.size.height + " " + bc.size.depth);
            if (isBox){
                b.setAttribute("onmouseover", "window.x3dom_fill3d._onmouseover(this)");
                b.setAttribute("onmouseout", "window.x3dom_fill3d._onmouseout(this)");
            }
            s.appendChild(b);
            
            rootTransform.appendChild(t);
        },
        highlightBox: function(boxElm, isMouseEvent){
            //The highlight effect
            var m = boxElm.parentElement.getElementsByTagName(_CONSTANTS.tagMaterial)[0];   //Material Node
            var begin_transparency=m.getAttribute("transparency");
            var old_transparency=m.getAttribute("old_transparency");
            if (! old_transparency){
                var transparency=m.getAttribute("transparency");
                m.setAttribute("old_transparency", transparency);
            }
            m.setAttribute("transparency", _CONSTANTS.HighlightTransparency);
            //Callback
            if (_CONSTANTS.HighlightTransparency != begin_transparency){    //from normalize to highlight state
                var id = boxElm.getAttribute("id");
                var fill3d = _BOXID_FILL3D_MAPPING[id];
                if (fill3d){
                    fill3d.boxHighlightCallback(boxElm, true);
                }
            }
        },
        normalizeBox: function(boxElm, isMouseEvent){
            //Turn off highlight effect
            var m = boxElm.parentElement.getElementsByTagName(_CONSTANTS.tagMaterial)[0];   //Material Node
            var begin_transparency=m.getAttribute("transparency");
            var old_transparency=m.getAttribute("old_transparency");
            if (old_transparency){
                m.setAttribute("transparency", old_transparency);
            }
            //Callback
            if (_CONSTANTS.HighlightTransparency == begin_transparency){    //from highlight to normalize state
                var id = boxElm.getAttribute("id");
                var fill3d = _BOXID_FILL3D_MAPPING[id];
                if (fill3d){
                    fill3d.boxHighlightCallback(boxElm, false);
                }
            }
        }
    };
    
    var _FILL3D_COLLECTIONS = {};
    /**
     * Build a X3Dom Container-Fill operation object, based on the specified Transform element
     *      name - the name of current object
     *      rootTransform - the specified Transform element, or the element's id
     */
    var Fill3D = function(name, rootTransform){
        if ( typeof rootTransform === "string" ){   //A string means is a element id
            rootTransform = document.getElementById(rootTransform);
        }
        this.name = name;
        this.rootTransform = rootTransform;
        this.containerCfg = _UTILS.clone(_DEFAULT_CONTAINER_CFG);
        this.templateBoxCfg = _UTILS.clone(_DEFAULT_BOX_CFG);
        this.boxHighlightCallback = function(boxElm, isHighlight){/*Do nothing*/};
        
        this.allBoxIds = [];  //All Box's ID
        
        _FILL3D_COLLECTIONS[name] = this;
    };
    /**
     * Get the x3dom runtime object
     */
    Fill3D.prototype.getRuntime = function(){
        var x3d = this.rootTransform.parentElement.parentElement;
        return x3d.runtime;
    };
    /**
     * Create the container
     */
    Fill3D.prototype.createContainer = function(){
        var cc = this.containerCfg;
        var CP = _CONSTANTS.prefixContainter;
        _UTILS.addBox(this, {
            size: { width: cc.size.width, height: cc.size.sideSize, depth: cc.size.depth },
            position: { x: 0, y: 0, z: 0 },
            materialAttributes: cc.materialAttributes.bottom
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        _UTILS.addBox(this, {
            size: { width: cc.size.width, height: cc.size.height, depth: cc.size.sideSize },
            position: { x: 0, y: 0, z: 0 },
            materialAttributes: cc.materialAttributes.back
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        _UTILS.addBox(this, {
            size: { width: cc.size.sideSize, height: cc.size.height, depth: cc.size.depth },
            position: { x: 0, y: 0, z: 0 },
            materialAttributes: cc.materialAttributes.left
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        _UTILS.addBox(this, {
            size: { width: cc.size.width, height: cc.auxLine.size, depth: cc.auxLine.size },
            position: { x: 0, y: cc.size.height-cc.auxLine.size, z: cc.size.depth-cc.auxLine.size },
            materialAttributes: cc.auxLine.materialAttributes
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        _UTILS.addBox(this, {
            size: { width: cc.auxLine.size, height: cc.size.height, depth: cc.auxLine.size },
            position: { x: cc.size.width-cc.auxLine.size, y: 0, z: cc.size.depth-cc.auxLine.size },
            materialAttributes: cc.auxLine.materialAttributes
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        _UTILS.addBox(this, {
            size: { width: cc.auxLine.size, height: cc.auxLine.size, depth: cc.size.depth },
            position: { x: cc.size.width-cc.auxLine.size, y: cc.size.height-cc.auxLine.size, z: 0 },
            materialAttributes: cc.auxLine.materialAttributes
        }, false, CP+"_"+this.name+"_"+_UTILS.nextId());
        //reset to default rotation
        this.rootTransform.setAttribute('rotation', _GLOBAL_CFG.CONTAINER_ROTATIONS["default"]);
    };
    /**
     * Add a Box into container
     *      id: the id of box element, optional
     *      position: the position of box, ( {x: x_value, y: y_value, z: z_value} ), in meters
     *      size: the size of box, ( {width: width_value, height: height_value, depth: depth_value} ), in meters
     *      color: the color of box, optional
     */
    Fill3D.prototype.addBox = function(id, position, size, color){
        var cfg = _UTILS.clone(this.templateBoxCfg);
        if (position){
            if (null!=position.x) cfg.position.x = position.x;
            if (null!=position.y) cfg.position.y = position.y;
            if (null!=position.z) cfg.position.z = position.z;
        }
        if (size){
            if (null!=size.width) cfg.size.width = size.width;
            if (null!=size.height)cfg.size.height= size.height;
            if (null!=size.depth) cfg.size.depth = size.depth;
        }
        if (null == color){
            cfg.materialAttributes.diffuseColor = _UTILS.nextColor();
        }else{
            cfg.materialAttributes.diffuseColor = color;
        }
        if (null == id){
            id = _CONSTANTS.prefixBox+"_"+this.name+"_"+_UTILS.nextId();
        }
        
        _UTILS.addBox(this, cfg, true, id);
    };
    /**
     * Clean all boxes
     */
    Fill3D.prototype.cleanBoxes = function(){
        var _removeChild = function(elm){
            while (elm.firstChild) {
                _removeChild(elm.firstChild);
                elm.removeChild(elm.firstChild);
            }
        };
        _removeChild(this.rootTransform);

        //Unregister boxed
        for(var i=0; i<this.allBoxIds.length; i++){
            var deletedBoxId = this.allBoxIds[i];
            delete(_BOXID_FILL3D_MAPPING[deletedBoxId]);
        }
        this.allBoxIds = [];
    };
    /**
     * Rotate the viewport to a side.
     *      sideName: default, front, top, right
     */
    Fill3D.prototype.rotateToSide = function(sideName){
        this.rootTransform.setAttribute('rotation', _GLOBAL_CFG.CONTAINER_ROTATIONS[sideName]);
        this.getRuntime().resetView();
    };
    /**
     * Highlight a box
     */
    Fill3D.prototype.highlightBox = function(boxId){
        var boxes = this.rootTransform.getElementsByTagName(_CONSTANTS.tagBox);
        for (var i=0; i<boxes.length; i++){
            var box = boxes[i];
            var id = box.getAttribute("id");
            if (id){
                if (id==boxId){
                    _UTILS.highlightBox(box);
                }else{
                    _UTILS.normalizeBox(box);
                }
            }
        }
    };
    //Export ...
    if (! window.x3dom_fill3d){
        window.x3dom_fill3d = {
            buildFill3D: function(name, rootTransform){
                var x3dom = new Fill3D(name, rootTransform);
                return x3dom;
            },
            getFill3D: function(name){
                return _FILL3D_COLLECTIONS[name];
            },
            _onmouseover: function(boxElm){
                _UTILS.highlightBox(boxElm);
            },
            _onmouseout: function(boxElm){
                _UTILS.normalizeBox(boxElm);
            }
        };
    }
})();
