package com.intellij.javascript.flex.css;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.indexing.IndexableSetContributor;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Set;

/**
 * User: ksafonov
 */
public class FlexStylesIndexableSetContributor extends IndexableSetContributor {

  private static final Logger LOG = Logger.getInstance(FlexStylesIndexableSetContributor.class);

  private static final Set<VirtualFile> FILES;

  static {
    URL libFileUrl = FlexStylesIndexableSetContributor.class.getResource("FlexStyles.as");
    if ("file".equals(libFileUrl.getProtocol())) {
      VfsRootAccess.allowRootAccess(VfsUtilCore.urlToPath(VfsUtilCore.convertFromUrl(libFileUrl)));
    }
    VirtualFile file = VfsUtil.findFileByURL(libFileUrl);
    if (file == null) {
      LOG.error("Cannot find FlexStyles.as file by url " + VfsUtilCore.convertFromUrl(libFileUrl));
    }
    FILES = ContainerUtil.createMaybeSingletonSet(file);
  }

  @NotNull
  @Override
  public Set<VirtualFile> getAdditionalRootsToIndex() {
    return FILES;
  }

  public static GlobalSearchScope enlarge(final GlobalSearchScope scope) {
    return scope.union(new GlobalSearchScope() {
      @Override
      public boolean contains(@NotNull final VirtualFile file) {
        return FILES.contains(file);
      }

      @Override
      public int compare(@NotNull final VirtualFile file1, @NotNull final VirtualFile file2) {
        return scope.compare(file1, file2);
      }

      @Override
      public boolean isSearchInModuleContent(@NotNull final Module aModule) {
        return scope.isSearchInModuleContent(aModule);
      }

      @Override
      public boolean isSearchInLibraries() {
        return scope.isSearchInLibraries();
      }
    });
  }
}
