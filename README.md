MultiSelection
==============

An API for supporting selection of items. with Control and Shift operations, and double click as well.

- SetSelection is the top parent, just to keep a simple Set of selected items without events, or click processing.
- ClickSelection is a more specific layer that adds functionality for processing click events with Control and Shift modifiers. It also adds events with Google Guava.
- DoubleClickSelection just adds double click event on top of ClickSelection.

Check JUnits for a more detailed usage
