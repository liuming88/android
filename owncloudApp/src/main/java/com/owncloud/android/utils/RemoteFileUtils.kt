/**
 * ownCloud Android client application
 *
 * @author David González Verdugo
 * Copyright (C) 2019 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.utils

import com.owncloud.android.lib.common.OwnCloudClient
import com.owncloud.android.operations.common.UseCaseHelper

class RemoteFileUtils {
    companion object {
        /**
         * Checks if remotePath does not exist in the server and returns it, or adds
         * a suffix to it in order to avoid the server file is overwritten.
         *
         * @param ownCloudClient
         * @param remotePath
         * @return
         */
        fun getAvailableRemotePath(remotePath: String): String? {
            var checkExistsFile = existsFile(remotePath)
            if (!checkExistsFile) {
                return remotePath
            }
            val pos = remotePath.lastIndexOf(".")
            var suffix: String
            var extension = ""
            if (pos >= 0) {
                extension = remotePath.substring(pos + 1)
                remotePath.apply {
                    substring(0, pos)
                }
            }
            var count = 2
            do {
                suffix = " ($count)"
                checkExistsFile = if (pos >= 0) {
                    existsFile("${remotePath.substringBeforeLast('.', "")}$suffix.$extension")
                } else {
                    existsFile(remotePath + suffix)
                }
                count++
            } while (checkExistsFile)
            return if (pos >= 0) {
                "${remotePath.substringBeforeLast('.', "")}$suffix.$extension"
            } else {
                remotePath + suffix
            }
        }

        private fun existsFile(remotePath: String): Boolean {

            val useCaseHelper = UseCaseHelper()
            return useCaseHelper.checkPathExistence(remotePath).isSuccess
        }
    }
}
